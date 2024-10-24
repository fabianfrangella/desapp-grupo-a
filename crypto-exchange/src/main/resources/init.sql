SELECT 1;

INSERT INTO CRYPTO_USER (CREATED_AT, DELETED_AT, UPDATED_AT, CRYPTO_WALLET_ADDRESS, CVU, EMAIL, LAST_NAME, NAME, PASSWORD, POINTS) VALUES (CURRENT_TIMESTAMP,NULL,CURRENT_TIMESTAMP,'12345679','1111111111111111111111','fabi@test.com','Frangella','Fabian','{bcrypt}$2a$10$y5zuwUGdKKgAVQCXphtzKOMmH.8NiLCEJZPQ0XiezGvs7g6xYFlCi',0);
INSERT INTO CRYPTO_USER (CREATED_AT, DELETED_AT, UPDATED_AT, CRYPTO_WALLET_ADDRESS, CVU, EMAIL, LAST_NAME, NAME, PASSWORD, POINTS) VALUES (CURRENT_TIMESTAMP,NULL,CURRENT_TIMESTAMP,'12345679','1111111111111111111111','andy@test.com','Mora','Andres','{bcrypt}$2a$10$iZDqpk22QRevVh4KeH702..6PAfXHDOcW70sgkagbHficanLDvSuC',0);

INSERT INTO CRYPTO_ACTIVE (CREATED_AT, DELETED_AT, UPDATED_AT, QUANTITY, TYPE, USER_ID) VALUES
(CURRENT_TIMESTAMP,null,CURRENT_TIMESTAMP,10,0,1),
(CURRENT_TIMESTAMP,null,CURRENT_TIMESTAMP,10,1,1),
(CURRENT_TIMESTAMP,null,CURRENT_TIMESTAMP,10,2,1),
(CURRENT_TIMESTAMP,null,CURRENT_TIMESTAMP,10,3,1),
(CURRENT_TIMESTAMP,null,CURRENT_TIMESTAMP,10,4,1),
(CURRENT_TIMESTAMP,null,CURRENT_TIMESTAMP,10,5,1),
(CURRENT_TIMESTAMP,null,CURRENT_TIMESTAMP,10,6,1),
(CURRENT_TIMESTAMP,null,CURRENT_TIMESTAMP,10,7,1),
(CURRENT_TIMESTAMP,null,CURRENT_TIMESTAMP,10,8,1),
(CURRENT_TIMESTAMP,null,CURRENT_TIMESTAMP,10,9,1),
(CURRENT_TIMESTAMP,null,CURRENT_TIMESTAMP,10,10,1),
(CURRENT_TIMESTAMP,null,CURRENT_TIMESTAMP,10,11,1),
(CURRENT_TIMESTAMP,null,CURRENT_TIMESTAMP,10,12,1),
(CURRENT_TIMESTAMP,null,CURRENT_TIMESTAMP,10,13,1),
(CURRENT_TIMESTAMP,null,CURRENT_TIMESTAMP,10,0,2),
(CURRENT_TIMESTAMP,null,CURRENT_TIMESTAMP,10,1,2),
(CURRENT_TIMESTAMP,null,CURRENT_TIMESTAMP,10,2,2),
(CURRENT_TIMESTAMP,null,CURRENT_TIMESTAMP,10,3,2),
(CURRENT_TIMESTAMP,null,CURRENT_TIMESTAMP,10,4,2),
(CURRENT_TIMESTAMP,null,CURRENT_TIMESTAMP,10,5,2),
(CURRENT_TIMESTAMP,null,CURRENT_TIMESTAMP,10,6,2),
(CURRENT_TIMESTAMP,null,CURRENT_TIMESTAMP,10,7,2),
(CURRENT_TIMESTAMP,null,CURRENT_TIMESTAMP,10,8,2),
(CURRENT_TIMESTAMP,null,CURRENT_TIMESTAMP,10,9,2),
(CURRENT_TIMESTAMP,null,CURRENT_TIMESTAMP,10,10,2),
(CURRENT_TIMESTAMP,null,CURRENT_TIMESTAMP,10,11,2),
(CURRENT_TIMESTAMP,null,CURRENT_TIMESTAMP,10,12,2),
(CURRENT_TIMESTAMP,null,CURRENT_TIMESTAMP,10,13,2);

INSERT INTO CRYPTO_PRICE (CREATED_AT, DELETED_AT, UPDATED_AT, CRYPTO_CURRENCY_TYPE, PRICE, TIME)
SELECT CREATED_AT, DELETED_AT, UPDATED_AT, TYPE, 10.0, CREATED_AT FROM CRYPTO_ACTIVE WHERE USER_ID = 1;

