CREATE TABLE IF NOT EXISTS public.dontsmokeinfo (
    ID BIGSERIAL,
    ANNOTATION VARCHAR(100),
    TYPE SMALLINT,
    INFO VARCHAR(400),
    CONSTRAINT "ID_PK" PRIMARY KEY(ID),
    CONSTRAINT "ANNOTATION_UN" UNIQUE(ANNOTATION),
    CONSTRAINT "INFO_UN" UNIQUE(INFO)
) ;

--Тип 1 - ccылки
 insert into public.dontsmokeinfo (annotation,type,info)
 values('О ВРЕДЕ КУРЕНИЯ. ФБУЗ «Центр гигиенического образования населения» Роспотребнадзора',1,
 'https://cgon.rospotrebnadzor.ru/naseleniyu/zdorovyy-obraz-zhizni/o-vrede-kureniya')
 ON CONFLICT (info) DO NOTHING;
 

--Тип 2 - цитата
 insert into public.dontsmokeinfo (annotation,type,info)
 values('Жорж Сименон',2,
 'Начинаешь курить, чтобы доказать, что ты мужчина. Потом пытаешься доказать, что ты мужчина')
 ON CONFLICT (info) DO NOTHING;
 
 
 --Тип 3 - афоризм/поговорка
 insert into public.dontsmokeinfo (annotation,type,info)
 values('Народная поговорка',3,'Кто табачное зелье любит, тот сам себя губит')
 ON CONFLICT (info) DO NOTHING;
 
 
 
 --Тип 4 - анекдот
 insert into public.dontsmokeinfo (annotation,type,info)
 values('https://www.anekdot.ru/tags/%D1%81%D0%B8%D0%B3%D0%B0%D1%80%D0%B5%D1%82%D1%8B/?type=anekdots&sort=sum',4,
 'Василий Петрович из Калуги, собираясь на охоту, по ошибке взял сигареты сына, и, уже к обеду, на лесной поляне, застрелил трех жирафов.')
 ON CONFLICT (info) DO NOTHING;