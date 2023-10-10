package ru.neoflex.learning.creaditpipeline.deal.dictionary;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Theme {

    FINISH_REGISTRATION("Создание заявки", "Заявка успешно создана. Id заявки: %s"),
    CREATE_DOCUMENTS("Формирование документов", "Подан запрос на оформление документов по заявке id: %s"),
    SEND_DOCUMENTS("Отправка документов","Отправка документов по заявке id: %s"),
    SEND_SES("Ссылка на подписание", "Документы подписаны по заявке id: %s"),
    CREDIT_ISSUED("Кредит выдан", "Кредит выдан по заявке id: %s"),
    APPLICATION_DENIED("Отказ по заявке", "Заявка id: %s отклонена");

    private final String subject;
    private final String text;
}
