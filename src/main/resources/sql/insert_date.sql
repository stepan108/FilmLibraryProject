INSERT INTO roles (id, description, title)
SELECT 1, 'Роль пользователя', 'USER'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE id = 1)
UNION ALL
SELECT 2, 'Роль библиотекаря', 'MANAGER'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE id = 2);

--Режиссеры
drop sequence directors_sequence;
create sequence directors_sequence;
alter sequence directors_sequence owner to postgres;

truncate table films_directors;
truncate table directors cascade;
INSERT INTO directors (id, created_by, created_when, director_fio, biography, birth_date)
VALUES (nextval('directors_sequence'), 'admin', '2022-11-15 13:46:11.797607', 'Чарльз Чаплин', 'Англо-американский киноактер, сценарист, композитор и режиссер. Обладатель премии «Оскар». Наибольшую известность приобрел благодаря созданию образа бродяжки Чарли, выступающего в многочисленных короткометражных комедиях.', '1889-04-16');
INSERT INTO directors (id, created_by, created_when, director_fio, biography, birth_date)
VALUES (nextval('directors_sequence'), 'admin', '2022-11-15 13:47:02.414728', 'Серджо Леоне',
        'Итальянский кинорежиссёр, сценарист, продюсер. Известен как один из основателей жанра спагетти-вестерн, также широко известен как один из самых влиятельных режиссеров в истории кинематографа.',
        '1929-12-03');
INSERT INTO directors (id, created_by, created_when, director_fio, biography, birth_date)
VALUES (nextval('directors_sequence'), 'admin', '2022-11-15 13:48:53.363059', 'Квентин Тарантино',
        'Американский кинорежиссёр, сценарист, продюсер и актёр. Один из наиболее ярких представителей постмодернизма в независимом кинематографе.',
        '1963-03-27');
INSERT INTO directors (id, created_by, created_when, director_fio, biography, birth_date)
VALUES (nextval('directors_sequence'), 'admin', '2022-11-15 13:50:12.953413', 'Алексей Балабанов',
        'Советский и российский кинорежиссёр, сценарист, продюсер. Один из создателей кинокомпании «СТВ». Член Европейской киноакадемии.',
        '1959-02-25');
INSERT INTO directors (id, created_by, created_when, director_fio, biography, birth_date)
VALUES (nextval('directors_sequence'), 'admin', '2022-11-15 13:51:08.314682', 'Николас Виндинг Рефн',
        'Датский кинорежиссёр, сценарист, продюсер и актёр. Обладатель приза за лучшую режиссуру на 64-м Каннском кинофестивале, номинант на премию BAFTA в категории «Лучшая режиссёрская работа» за фильм «Драйв».',
        '1970-09-29');
INSERT INTO directors (id, created_by, created_when, director_fio, biography, birth_date)
VALUES (nextval('directors_sequence'), 'admin', '2022-11-15 13:51:08.314682', 'Уэс Крэйвен', 'Американский кинорежиссёр, продюсер и сценарист, известный своими работами в жанре ужасов, в особенности — слэшерах.', '1939-08-02');
INSERT INTO directors (id, created_by, created_when, director_fio, biography, birth_date)
VALUES (nextval('directors_sequence'), 'admin', '2022-11-15 13:51:08.314682', 'Итан Коэн', 'Известный голливудский режиссер, сценарист и продюсер, работающий в дуэте со старшим братом Джоэлом Коэном.', '1957-09-21');
INSERT INTO directors (id, created_by, created_when, director_fio, biography, birth_date)
VALUES (nextval('directors_sequence'), 'admin', '2022-11-15 13:51:08.314682', 'Джоэл Коэн', 'Известный голливудский режиссер, сценарист и продюсер, работающий в дуэте со младшим братом Итаном Коэном.', '1954-11-29');


--Фильмы
drop sequence films_sequence;
create sequence films_sequence;
alter sequence films_sequence owner to postgres;
truncate table films cascade;

INSERT INTO films (id, created_by, created_when, title, premier_year, country, min_length, description, genre, online_copy_path)
VALUES (nextval('films_sequence'), 'admin', '2022-11-15 13:54:28.115079', 'Огни Большого города', '1931-12-30', 'США', 87, 'Маленький Бродяга встречает красивую слепую девушку, торгующую цветами на улице, которая по ошибке принимает его за богатого герцога. Узнав о том, что операция может вернуть ей зрение, маленький Бродяга пускается на поиски денег.', 5, null);
INSERT INTO films (id, created_by, created_when, title, premier_year, country, min_length, description, genre, online_copy_path)
VALUES (nextval('films_sequence'), 'admin', '2022-11-15 13:56:12.600618', 'Хороший, плохой, злой', '1966-12-23','Италия', 178, 'Разгар Гражданской войны, Дикий Запад. По воле судьбы трое мужчин вынуждены объединить усилия в поисках украденного золота. Но совместная работа — не самое подходящее занятие для таких отъявленных бандитов.', 4, null);
INSERT INTO films (id, created_by, created_when, title, premier_year, country, min_length, description, genre, online_copy_path)
VALUES (nextval('films_sequence'), 'admin', '2022-11-15 13:57:05.231780', 'Бешеные псы', '1992-12-21', 'США', 100, 'Это должно было стать идеальным преступлением. Задумав ограбить ювелирный магазин, криминальный босс Джо Кэбот собрал вместе шестерых опытных и совершенно незнакомых друг с другом преступников.', 4, null);
INSERT INTO films (id, created_by, created_when, title, premier_year, country, min_length, description, genre, online_copy_path)
VALUES (nextval('films_sequence'), 'admin', '2022-11-15 13:57:43.883671', 'Брат', '1997-12-12', 'Россия', 100, 'Демобилизованный из армии, Данила Багров вернулся в родной городок. Но скучная жизнь российской провинции не устраивала его, и он решился податься в Петербург, где, по слухам, уже несколько лет процветает его старший брат.', 4, null);
INSERT INTO films (id, created_by, created_when, title, premier_year, country, min_length, description, genre, online_copy_path)
VALUES (nextval('films_sequence'), 'admin', '2022-11-15 13:58:12.172216', 'Драйв', '2011-05-20', 'США', 100, 'Великолепный водитель – при свете дня он выполняет каскадерские трюки на съёмочных площадках Голливуда, а по ночам ведет рискованную игру. Но один опасный контракт – и за его жизнь назначена награда.', 4, null);
INSERT INTO films (id, created_by, created_when, title, premier_year, country, min_length, description, genre, online_copy_path)
VALUES (nextval('films_sequence'), 'admin', '2022-11-15 13:58:12.172216', 'Кошмар на улице Вязов', '1984-11-10', 'США', 92, 'Четырём подросткам Спрингвуда снится один и тот же кошмарный сон, в котором за ними гонится и пытается убить человек с обожжённым лицом в шляпе и с ножами на руке.', 3, null);
INSERT INTO films (id, created_by, created_when, title, premier_year, country, min_length, description, genre, online_copy_path)
VALUES (nextval('films_sequence'), 'admin', '2022-11-15 13:58:12.172216', 'Большой Лебовски', '1998-01-18', 'США', 117, 'Лос-Анджелес, 1991 год, война в Персидском заливе. Главный герой по прозвищу Чувак считает себя совершенно счастливым человеком. Его жизнь составляют игра в боулинг и выпивка. ', 5, null);

--FILMS_DIRECTORS
INSERT INTO public.films_directors (film_id, director_id)
VALUES (1, 1);
INSERT INTO public.films_directors (film_id, director_id)
VALUES (2, 2);
INSERT INTO public.films_directors (film_id, director_id)
VALUES (3, 3);
INSERT INTO public.films_directors (film_id, director_id)
VALUES (4, 4);
INSERT INTO public.films_directors (film_id, director_id)
VALUES (5, 5);
INSERT INTO public.films_directors (film_id, director_id)
VALUES (6, 6);
INSERT INTO public.films_directors (film_id, director_id)
VALUES (7, 7);
INSERT INTO public.films_directors (film_id, director_id)
VALUES (7, 8);