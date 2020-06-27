package com.kderyabin.core.services;

import com.kderyabin.core.model.*;
import com.kderyabin.core.storage.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import com.kderyabin.core.storage.repository.*;

/**
 * StorageManager is implementing a facade design pattern facilitating work with
 * database.
 * 
 * @author Konstantin Deryabin
 */
@Service
public class StorageManager {

	final private Logger LOG = LoggerFactory.getLogger(StorageManager.class);

	BoardRepository boardRepository;
	PersonRepository personRepository;
	BoardItemRepository itemRepository;
	SettingRepository settingRepository;
	UserRepository userRepository;
	
	/**
	 * Finds user in DB by login and password.
	 * @param login User login.
	 * @param password user password.
	 * @return UserModel instance or null if user is not found.
	 */
	public UserModel findUserByLoginPassword(String login, String password) {
    	UserEntity entity = userRepository.findByLoginPwd(login, password);
    	return getModel( entity);
	}

	@Transactional
	public List<BoardPersonTotal> getBoardPersonTotal(long boardId) {
		List<BoardPersonTotal> result = new ArrayList<>();
		itemRepository.getBoardPersonTotal(boardId).stream().forEach(row -> {
			BoardPersonTotal item = new BoardPersonTotal((BigDecimal) row[0], (Long) row[1], (String) row[2],
					(Long) row[3]);
			result.add(item);
		});
		return result;
	}

	@Transactional
	public List<BoardModel> getRecentBoards(int limit) {

		return boardRepository.loadRecent(limit).stream().map(this::getModel).collect(Collectors.toList());
	}

	@Transactional
	public List<BoardModel> getBoards() {
		List<BoardModel> result = new LinkedList<>();
		boardRepository.findAll().forEach(entity -> result.add(getModel(entity)));
		return result;
	}

	@Transactional
	public BoardModel loadParticipants(BoardModel model) {
		LOG.debug("loadParticipants: Participants in model before fetching:" + model.getParticipants().size());
		model.getParticipants().clear();
		personRepository.findAllByBoardId(model.getId()).forEach(e -> model.addParticipant(getModel(e)));
		LOG.debug("loadParticipants: Participants in model after fetching:" + model.getParticipants().size());
		return model;
	}

	@Transactional
	public List<PersonModel> getParticipants(BoardModel model) {
		List<PersonModel> result = new ArrayList<>();
		personRepository.findAllByBoardId(model.getId()).forEach(e -> result.add(getModel(e)));

		return result;
	}

	@Transactional
	public List<PersonModel> getPersons() {
		return personRepository.findAll().stream().map(this::getModel).collect(Collectors.toList());
	}

	@Transactional
	public PersonModel save(PersonModel model) {
		LOG.debug("Start PersonModel saving ");
		PersonEntity entity = getEntity(model);
		entity = personRepository.saveAndFlush(entity);
		model = getModel(entity);
		LOG.debug("End PersonModel saving");
		return model;
	}

	@Transactional
	public BoardModel save(BoardModel model, boolean participants) {
		LOG.debug("Start board saving ");
		LOG.debug("Participants size:" + model.getParticipants().size());
		BoardEntity entity = getEntity(model);
		if (participants) {
			for (PersonModel person : model.getParticipants()) {
				PersonEntity pe = getEntity(person);
				// Should persist new entity?
				if (pe.getId() == null) {
					pe = personRepository.save(pe);
					person.setId(pe.getId());
				}
				// add to the board
				entity.addParticipant(pe);
			}
			LOG.debug("Participants: " + model.getParticipants().toString());
		}

		entity.initUpdateTime();
		LOG.debug("Entity: " + entity.toString());
		entity = boardRepository.saveAndFlush(entity);
		BoardModel result = getModel(entity);
		if (participants) {
			result.setParticipants(model.getParticipants());
		}
		LOG.debug("Entity saved ");
		LOG.debug("Participants in returned model: " + result.getParticipants().toString());
		return result;
	}

	@Transactional
	public void save(BoardItemModel model) {
		LOG.debug("BoardItemModel: save participants size: " + model.getBoard().getParticipants().size());
		BoardItemEntity entity = getEntity(model);
		// Reload the board
		LOG.debug("BoardItemModel: Reload the board: " + model.getBoard().getId());
		BoardEntity board = boardRepository.getOne(model.getBoard().getId());
		board.initUpdateTime();
		board = boardRepository.save(board);
		entity.setBoard(board);
		// reload participants
		LOG.debug("BoardItemModel: Reload the participant: " + model.getPerson().getId());
		PersonEntity person = personRepository.getOne(model.getPerson().getId());
		entity.setPerson(person);

		LOG.debug(">>> Start BoardItemModel saving ");
		itemRepository.saveAndFlush(entity);
		LOG.debug(">>> Saved BoardItemModel ");
	}

	@Transactional(readOnly = true)
	public BoardModel loadItems(BoardModel model) {
		List<BoardItemEntity> items = itemRepository.findAllByBoardId(model.getId());
		if (!items.isEmpty()) {
			model.setItems(items.stream().map(e -> {
				BoardItemModel i = getModel(e);
				i.setBoard(model);
				return i;
			}).collect(Collectors.toList()));
		}
		return model;
	}

	/**
	 * Fetches board items.
	 *
	 * @param model Instance of BoardModel
	 * @return List of BoardItemModels
	 */
	@Transactional(readOnly = true)
	public List<BoardItemModel> getItems(BoardModel model) {
		List<BoardItemEntity> items = itemRepository.findAllByBoardId(model.getId());
		List<BoardItemModel> result = new ArrayList<>();
		if (!items.isEmpty()) {
			items.forEach(e -> {
				BoardItemModel m = getModel(e);
				m.setBoard(model);
				result.add(m);
			});
		}
		return result;
	}

	/**
	 * Retrieves a list of SettingModel objects.
	 * 
	 * @return list of stored settings.
	 */
	@Transactional(readOnly = true)
	public List<SettingModel> getSettings() {
		List<SettingModel> result = new ArrayList<>();
		List<SettingEntity> items = settingRepository.findAll();
		if (!items.isEmpty()) {
			result.addAll(items.stream().map(this::getModel).collect(Collectors.toList()));
		}

		return result;
	}

	/**
	 * Saves a list of models in DB and returns a new list of models synced with DB.
	 * 
	 * @param list List of SettingModel
	 * @return List of SettingModel or empty list in case of error.
	 */
	@Transactional
	public List<SettingModel> save(List<SettingModel> list) {
		List<SettingEntity> entities, saved;
		entities = list.stream().map(this::getEntity).collect(Collectors.toList());

		saved = settingRepository.saveAll(entities);
		return saved.stream().map(this::getModel).collect(Collectors.toList());

	}

	@Transactional
	public void removeBoard(BoardModel board) {
		boardRepository.deleteById(board.getId());
	}

	// Converters

	public BoardEntity getEntity(BoardModel model) {
		BoardEntity entity = new BoardEntity();
		entity.setId(model.getId());
		entity.setName(model.getName());
		entity.setDescription(model.getDescription());
		entity.setCreation(model.getCreation());
		entity.setUpdate(model.getUpdate());
		entity.setCurrency(model.getCurrencyCode());
		return entity;
	}

	public BoardModel getModel(BoardEntity entity) {
		BoardModel model = new BoardModel();
		model.setId(entity.getId());
		model.setName(entity.getName());
		model.setDescription(entity.getDescription());
		model.setCreation(entity.getCreation());
		model.setUpdate(entity.getUpdate());
		model.setCurrency(entity.getCurrency());

		return model;
	}

	public PersonEntity getEntity(PersonModel model) {
		PersonEntity entity = new PersonEntity();
		entity.setId(model.getId());
		entity.setName(model.getName());

		return entity;
	}

	public PersonModel getModel(PersonEntity source) {
		PersonModel target = new PersonModel();
		target.setId(source.getId());
		target.setName(source.getName());

		return target;
	}

	public BoardItemEntity getEntity(BoardItemModel source) {
		BoardItemEntity target = new BoardItemEntity();
		target.setId(source.getId());
		target.setTitle(source.getTitle());
		target.setAmount(source.getAmount());
		target.setDate(source.getDate());
		target.setPerson(getEntity(source.getPerson()));
		target.setBoard(getEntity(source.getBoard()));
		return target;
	}

	public BoardItemModel getModel(BoardItemEntity source) {
		BoardItemModel target = new BoardItemModel();
		target.setId(source.getId());
		target.setTitle(source.getTitle());
		target.setAmount(source.getAmount());
		target.setDate(source.getDate());
		target.setPerson(getModel(source.getPerson()));
		target.setBoard(getModel(source.getBoard()));
		return target;
	}

	public SettingEntity getEntity(SettingModel source) {
		SettingEntity target = new SettingEntity();
		target.setId(source.getId());
		target.setName(source.getName());
		target.setValue(source.getValue());

		return target;
	}

	public SettingModel getModel(SettingEntity source) {
		SettingModel target = new SettingModel();
		target.setId(source.getId());
		target.setName(source.getName());
		target.setValue(source.getValue());

		return target;
	}
	
	/**
	 * Converts UserEntity into UserModel.
	 * @param entity UserEntity instance. 
	 * @return UserModel instance.
	 */
	public UserModel getModel( UserEntity source) {
		
		if( source == null) {
			return null;
		}
		
		UserModel target = new UserModel();
		target.setId(source.getId());
		target.setLogin(source.getLogin());
		target.setPwd(source.getPwd());
		target.setToken(source.getToken());
		
		return target;
	}
	
	/**
	 * Converts UserModel into UserEntity.
	 * @param entity UserModel instance. 
	 * @return UserEntity instance or null if source parameter is null.
	 */
	public UserEntity getEntity( UserModel source) {
		
		if( source == null) {
			return null;
		}
		
		UserEntity target = new UserEntity();
		target.setId(source.getId());
		target.setLogin(source.getLogin());
		target.setPwd(source.getPwd());
		target.setToken(source.getToken());
		
		return target;
	}
	/*
	 * Getters / Setters
	 */

	@Autowired
	public void setBoardRepository(BoardRepository boardRepository) {
		this.boardRepository = boardRepository;
	}

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Autowired
	public void setPersonRepository(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	@Autowired
	public void setItemRepository(BoardItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	@Autowired
	public void setSettingRepository(SettingRepository settingRepository) {
		this.settingRepository = settingRepository;
	}

}
