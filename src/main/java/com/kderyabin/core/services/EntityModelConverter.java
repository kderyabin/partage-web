package com.kderyabin.core.services;

import com.kderyabin.core.model.BoardItemModel;
import com.kderyabin.core.model.BoardModel;
import com.kderyabin.core.model.PersonModel;
import com.kderyabin.core.model.SettingModel;
import com.kderyabin.core.storage.entity.BoardEntity;
import com.kderyabin.core.storage.entity.BoardItemEntity;
import com.kderyabin.core.storage.entity.PersonEntity;
import com.kderyabin.core.storage.entity.SettingEntity;

/**
 * Converts entity instance to model instance and vice versa.
 */
public class EntityModelConverter {

    /**
     * Converts BoardModel instance into BoardEntity instance.
     * Important: the method does not handle participants and items.
     * They must be handled individually case by case.
     * @param model BoardModel
     * @return      BoardEntity
     */
    public static BoardEntity getEntity(BoardModel model) {
        BoardEntity entity = new BoardEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        entity.setCreation(model.getCreation());
        entity.setUpdate(model.getUpdate());
        entity.setCurrency(model.getCurrencyCode());
        return entity;
    }

    /**
     * Converts BoardEntity into BoardModel.
     * Important: the method does not handle participants and items.
     * They must be handled individually case by case.
     * @param entity    BoardEntity instance
     * @return          BoardModel instance
     */
    public static BoardModel getModel(BoardEntity entity) {
        BoardModel model = new BoardModel();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        model.setCreation(entity.getCreation());
        model.setUpdate(entity.getUpdate());
        model.setCurrency(entity.getCurrency());

        return model;
    }

    /**
     * Converts PersonModel into PersonEntity.
     * Important: the method does not handle person's items.
     * If required, they must be handled a part.
     * @param model PersonModel instance
     * @return      PersonEntity instance
     */
    public static PersonEntity getEntity(PersonModel model) {
        PersonEntity entity = new PersonEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());

        return entity;
    }

    /**
     * ConvertsPersonEntity into PersonModel.
     * Important: the method does not handle person's items.
     * If required, they must be handled a part.
     * @param source    PersonEntity instance
     * @return          PersonModel instance
     */
    public static PersonModel getModel(PersonEntity source) {
        PersonModel target = new PersonModel();
        target.setId(source.getId());
        target.setName(source.getName());

        return target;
    }

    /**
     * Converts BoardItemModel into BoardItemEntity.
     * @param source    BoardItemModel instance
     * @return          BoardItemEntity instance
     */
    public static BoardItemEntity getEntity(BoardItemModel source) {
        BoardItemEntity target = new BoardItemEntity();
        target.setId(source.getId());
        target.setTitle(source.getTitle());
        target.setAmount(source.getAmount());
        target.setDate(source.getDate());
        target.setPerson(getEntity(source.getPerson()));
        target.setBoard(getEntity(source.getBoard()));
        return target;
    }

    /**
     * Converts BoardItemEntity into BoardItemModel.
     * @param source    BoardItemEntity instance
     * @return          BoardItemModel instance
     */
    public static BoardItemModel getModel(BoardItemEntity source) {
        BoardItemModel target = new BoardItemModel();
        target.setId(source.getId());
        target.setTitle(source.getTitle());
        target.setAmount(source.getAmount());
        target.setDate(source.getDate());
        target.setPerson(getModel(source.getPerson()));
        target.setBoard(getModel(source.getBoard()));
        return target;
    }

    /**
     * Converts SettingModel into SettingEntity.
     * @param source    SettingModel instance
     * @return          SettingEntity instance
     */
    public static SettingEntity getEntity(SettingModel source) {
        SettingEntity target = new SettingEntity();
        target.setId(source.getId());
        target.setName(source.getName());
        target.setValue(source.getValue());

        return target;
    }

    /**
     * Converts SettingEntity into SettingModel
     * @param source    SettingEntity instance
     * @return          SettingModel instance
     */
    public static SettingModel getModel(SettingEntity source) {
        SettingModel target = new SettingModel();
        target.setId(source.getId());
        target.setName(source.getName());
        target.setValue(source.getValue());

        return target;
    }
}
