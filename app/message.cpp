#include "message.h"

Message::Message()
{

}

QString Message::getMessage() const
{
    return "<h4>Сообщение от: "+author+" Дата: "+date+"</h4><h4>Тема: "+subject+"</h4><hr/>"+text;
}

QString Message::getTitle() const
{
    return date+" / "+subject;
}
