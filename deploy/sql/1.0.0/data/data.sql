insert into LESSON_GROUP(LG_ID,LG_NAME) values (1, 'www.ajslovicka.cz');
update LESSON set L_TOTAL_COUNT = (select t_count from total_view where t_name = l_name);


insert into CONFIG (C_ID,C_NAME,C_TYPE,C_VALUE) values (100,'matchedWordCount','N',1); 
insert into CONFIG (C_ID,C_NAME,C_TYPE,C_VALUE) values (200,'colorMode','N',1); 
insert into CONFIG (C_ID,C_NAME,C_TYPE,C_VALUE) values (300,'pronanciationUrl','S','http://api.naturalreaders.com/v2/tts/?t=REPLACE&r=30&s=0'); 
insert into CONFIG (C_ID,C_NAME,C_TYPE,C_VALUE) values (400,'pronanciationUrlCzech','S','http://api.naturalreaders.com/v2/tts/?t=REPLACE&r=12&s=-2'); 
insert into CONFIG (C_ID,C_NAME,C_TYPE,C_VALUE) values (500,'proxy','S','tmg.muzo.com'); 
insert into CONFIG (C_ID,C_NAME,C_TYPE,C_VALUE) values (600,'proxyPort','S','8080'); 