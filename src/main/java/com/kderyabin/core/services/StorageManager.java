package com.kderyabin.core.services;

import com.kderyabin.core.model.*;
import com.kderyabin.core.storage.entity.BoardEntity;
import com.kderyabin.core.storage.entity.BoardItemEntity;
import com.kderyabin.core.storage.entity.PersonEntity;
import com.kderyabin.core.storage.entity.SettingEntity;
import com.kderyabin.core.storage.repository.BoardItemRepository;
import com.kderyabin.core.storage.repository.BoardRepository;
import com.kderyabin.core.storage.repository.PersonRepository;
import com.kderyabin.core.storage.repository.SettingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * StorageManager implements a facade design pattern facilitating the work with different repositories.
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

    /*
     * Getters / Setters
     */
    @Autowired
    public void setBoardRepository(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
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

    /**
     * Fetches total amount spent by every participant.
     *
     * @param boardId Board ID
     * @return List of BoardPersonTotal instances
     */
    @Transactional(readOnly = true)
    public List<BoardPersonTotal> getBoardPersonTotal(long boardId) {
        List<BoardPersonTotal> result = new ArrayList<>();
        itemRepository.getBoardPersonTotal(boardId).forEach(row -> {
            BoardPersonTotal item = new BoardPersonTotal(
                    (BigDecimal) row[0],
                    Long.valueOf(row[1].toString()),
                    (String) row[2],
                    Long.valueOf(row[3].toString()));
            result.add(item);
        });
        return result;
    }

    /**
     * Fetches passed in parameter number of boards sorted by update date.
     *
     * @param limit Number of board to fetch
     * @return List of boards
     */
    @Transactional(readOnly = true)
    public List<BoardModel> getRecentBoards(int limit) {
        return boardRepository.loadRecent(limit).stream().map(EntityModelConverter::getModel).collect(Collectors.toList());
    }

    /**
     * Fetches all user's boards.
     *
     * @return List of boards
     */
    @Transactional(readOnly = true)
    public List<BoardModel> getBoards() {
        List<BoardModel> result = new LinkedList<>();
        List<BoardEntity> list = boardRepository.findAll();
        list.forEach(entity -> result.add(EntityModelConverter.getModel(entity)));
        return result;
    }

    /**
     * Get BoardModel by ID.
     *
     * @return BoardModel instance or null if not found
     */
    @Transactional(readOnly = true)
    public BoardModel findBoardById(Long id) {
        Optional<BoardEntity> entity = boardRepository.findById(id);
        return entity.map(EntityModelConverter::getModel).orElse(null);
    }

    /**
     * Fetches board's participants and appends them to the board model.
     *
     * @param model BoardModel instance
     * @return Same instance with participants
     */
    @Transactional(readOnly = true)
    public BoardModel loadParticipants(BoardModel model) {
        LOG.debug("loadParticipants: Participants in model before fetching:" + model.getParticipants().size());
        model.getParticipants().clear();
        personRepository.findAllByBoardId(model.getId()).forEach(e -> model.addParticipant(EntityModelConverter.getModel(e)));
        LOG.debug("loadParticipants: Participants in model after fetching:" + model.getParticipants().size());
        return model;
    }

    /**
     * Fetches board's participants
     *
     * @param model BoardModel instance
     * @return List of participants
     */
    @Transactional(readOnly = true)
    public List<PersonModel> getParticipants(BoardModel model) {
        return getParticipantsByBoardId(model.getId());
    }

    /**
     * Fetches board's participants by board ID.
     *
     * @param id Board ID.
     * @return List of participants
     */
    @Transactional(readOnly = true)
    public List<PersonModel> getParticipantsByBoardId(Long id) {
        List<PersonModel> result = new ArrayList<>();
        personRepository.findAllByBoardId(id).forEach(e -> result.add(EntityModelConverter.getModel(e)));

        return result;
    }

    /**
     * Fetches all registered in application participants.
     *
     * @return List of participants
     */
    @Transactional
    public List<PersonModel> getPersons() {
        return personRepository.findAll().stream().map(EntityModelConverter::getModel).collect(Collectors.toList());
    }

    /**
     * Fetches person by its ID
     *
     * @param id Person ID
     * @return PersonModel instance or null if the person is not found.
     */
    @Transactional(readOnly = true)
    public PersonModel findPersonById(Long id) {
        Optional<PersonEntity> entity = personRepository.findById(id);
        if (entity.isEmpty()) {
            return null;
        }
        return EntityModelConverter.getModel(entity.get());
    }

    /**
     * Creates or updates a PersonModel instance in database.
     *
     * @param model PersonModel instance
     * @return New PersonModel's instance, the copy of what is saved in database.
     */
    @Transactional
    public PersonModel save(PersonModel model) {
        LOG.debug("Start PersonModel saving ");
        PersonEntity entity = EntityModelConverter.getEntity(model);
        entity = personRepository.saveAndFlush(entity);
        model = EntityModelConverter.getModel(entity);
        LOG.debug("End PersonModel saving");
        return model;
    }

    /**
     * Creates or updates a BoardModel instance with participants in database.
     * Participants' saving is optional.
     *
     * @param model        BoardModel instance
     * @param participants Flag saying if attached participants must be saved as well.
     * @return
     */
    @Transactional
    public BoardModel save(BoardModel model, boolean participants) {
        LOG.debug("Start board saving ");
        LOG.debug("Participants size:" + model.getParticipants().size());
        BoardEntity entity = EntityModelConverter.getEntity(model);
        if (participants) {
            for (PersonModel person : model.getParticipants()) {
                PersonEntity pe = EntityModelConverter.getEntity(person);
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
        BoardModel result = EntityModelConverter.getModel(entity);
        if (participants) {
            result.setParticipants(model.getParticipants());
        }
        LOG.debug("Entity saved ");
        LOG.debug("Participants in returned model: " + result.getParticipants().toString());
        return result;
    }

    /**
     * Creates or updated a BoardItemModel instance in database.
     *
     * @param model BoardItemModel instance
     */
    @Transactional
    public void save(BoardItemModel model) {
        LOG.debug("Start BoardItemModel save");
        BoardItemEntity entity = EntityModelConverter.getEntity(model);
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

    /**
     * Fetches board's items in database and append them to the passed in parameter BoardModel instance
     *
     * @param model BoardModel instance
     * @return BoardModel with attached items
     */
    @Transactional(readOnly = true)
    public BoardModel loadItems(BoardModel model) {
        List<BoardItemModel> itemsList = getItems(model);
        if (!itemsList.isEmpty()) {
            model.setItems(itemsList);
        }
        return model;
    }

    /**
     * Fetches board's items in database
     *
     * @param model BoardModel instance
     * @return List of items.
     */
    @Transactional(readOnly = true)
    public List<BoardItemModel> getItems(BoardModel model) {
        List<BoardItemEntity> items = itemRepository.findAllByBoardId(model.getId());
        List<BoardItemModel> result = new ArrayList<>();
        if (!items.isEmpty()) {
            items.forEach(e -> {
                BoardItemModel m = EntityModelConverter.getModel(e);
                m.setBoard(model);
                result.add(m);
            });
        }
        return result;
    }

    /**
     * Fetches item in database by its ID.
     *
     * @param id Item id
     * @return Model instance or null id item is not found.
     */
    @Transactional(readOnly = true)
    public BoardItemModel findItemById(Long id) {
        Optional<BoardItemEntity> option = itemRepository.findById(id);
        return option.map(EntityModelConverter::getModel).orElse(null);
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
            result.addAll(items.stream().map(EntityModelConverter::getModel).collect(Collectors.toList()));
        }

        return result;
    }

    /**
     * Saves a list of SettingModel instances in database and returns a new list of models synced with DB.
     *
     * @param list List of SettingModel instances.
     * @return List of SettingModel or empty list in case of error.
     */
    @Transactional
    public List<SettingModel> save(List<SettingModel> list) {
        List<SettingEntity> entities, saved;
        entities = list.stream().map(EntityModelConverter::getEntity).collect(Collectors.toList());

        saved = settingRepository.saveAll(entities);
        return saved.stream().map(EntityModelConverter::getModel).collect(Collectors.toList());

    }

    /**
     * Removes board from database.
     * As database is relational that will removed related items and participants.
     *
     * @param board BoardModel instance
     */
    @Transactional
    public void removeBoard(BoardModel board) {
        boardRepository.deleteById(board.getId());
    }

    /**
     * Removes person from database.
     * This will remove related items and board belonging
     *
     * @param model PersonModel instance
     */
    @Transactional
    public void removePerson(PersonModel model) {
        personRepository.deleteById(model.getId());
    }

    /**
     * Removes persons items for some board.
     * @deprecated Replaced by database trigger
     * @param persons List of persons ID
     * @param boardId Board ID
     */
    @Deprecated
    @Transactional
    public void removePersonItems(List<Long> persons, Long boardId) {
        LOG.debug("Start removePersonItems");
        LOG.debug("Items to remove for persons: " + persons.toString());
        LOG.debug("Items to remove for Board: " + boardId);
        persons.forEach(p -> itemRepository.removeByBoardAndPerson(boardId, p));
    }
}
