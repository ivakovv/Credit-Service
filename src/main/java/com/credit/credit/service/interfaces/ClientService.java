package com.credit.credit.service.interfaces;

import com.credit.credit.dto.client.CreateClientRequestDto;
import com.credit.credit.dto.client.UpdateClientRequestDto;
import com.credit.credit.entity.Client;

public interface ClientService {

    /**
     * @author Ivakov Andrey
     * <p>
     *     <i> <b> Метод для создания нового клиента </b> </i>
     * </p>
     * @param request: dto для создания клиента
     * @see CreateClientRequestDto
     * @return В случае успешного создания возращает клиента
     * @see  Client
     * @exception: Если телефон уже есть, то выбрасывается исключение
     */
    Client createClient(CreateClientRequestDto request);

    /**
     * @author Ivakov Andrey
     * <p>
     *     <i> <b> Метод  обновляет поля клиента </b> </i>
     * </p>
     * Может обновлять одно или несколько полей
     * @param id: id клиента в системе
     * @param request: dto для обновления полей клиента
     * @see UpdateClientRequestDto
     * @return
     */
    Client updateClient(Long id, UpdateClientRequestDto request);

    /**
     * @author Ivakov Andrey
     * <p>
     *     <i> <b> Метод  для получения клиента </b> </i>
     * </p>
     * @param id: id клиента в системе
     * @return Если клиент найден, то возращает его
     * @see Client
     * @exception: Если клиент не найден, то выбрасывается исключение
     */
    Client getClient(Long id);
}
