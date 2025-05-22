--
-- PostgreSQL database dump
--

-- Dumped from database version 17.5
-- Dumped by pg_dump version 17.4

-- Started on 2025-05-21 22:53:56

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 6 (class 2615 OID 66170)
-- Name: bookstore; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA bookstore;


ALTER SCHEMA bookstore OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 66172)
-- Name: author_id_seq; Type: SEQUENCE; Schema: bookstore; Owner: bookstoreadmin
--

CREATE SEQUENCE bookstore.author_id_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE bookstore.author_id_seq OWNER TO bookstoreadmin;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 221 (class 1259 OID 66179)
-- Name: author; Type: TABLE; Schema: bookstore; Owner: bookstoreadmin
--

CREATE TABLE bookstore.author (
    id bigint DEFAULT nextval('bookstore.author_id_seq'::regclass) NOT NULL,
    code character varying(30) NOT NULL,
    name character varying(30) NOT NULL,
    last_modification_date timestamp without time zone,
    last_modification_user character varying(20)
);


ALTER TABLE bookstore.author OWNER TO bookstoreadmin;

--
-- TOC entry 218 (class 1259 OID 66171)
-- Name: book_id_seq; Type: SEQUENCE; Schema: bookstore; Owner: bookstoreadmin
--

CREATE SEQUENCE bookstore.book_id_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE bookstore.book_id_seq OWNER TO bookstoreadmin;

--
-- TOC entry 220 (class 1259 OID 66173)
-- Name: book; Type: TABLE; Schema: bookstore; Owner: bookstoreadmin
--

CREATE TABLE bookstore.book (
    id bigint DEFAULT nextval('bookstore.book_id_seq'::regclass) NOT NULL,
    code character varying(30) NOT NULL,
    title character varying(50) NOT NULL,
    date_of_publication date NOT NULL,
    synopsis character varying(300),
    number_of_pages smallint,
    last_modification_date timestamp without time zone,
    last_modification_user character varying(20)
);


ALTER TABLE bookstore.book OWNER TO bookstoreadmin;

--
-- TOC entry 222 (class 1259 OID 66185)
-- Name: book_author; Type: TABLE; Schema: bookstore; Owner: bookstoreadmin
--

CREATE TABLE bookstore.book_author (
    book_id bigint NOT NULL,
    author_id bigint NOT NULL
);


ALTER TABLE bookstore.book_author OWNER TO bookstoreadmin;

--
-- TOC entry 4811 (class 0 OID 66179)
-- Dependencies: 221
-- Data for Name: author; Type: TABLE DATA; Schema: bookstore; Owner: bookstoreadmin
--

COPY bookstore.author (id, code, name, last_modification_date, last_modification_user) FROM stdin;
402	2DNYD	Miguel de Cervantes	2025-05-21 21:47:10.988649	\N
403	STAYH	Gabriel García Márquez	2025-05-21 21:48:34.544793	\N
404	HN630	F. Scott Fitzgerald	2025-05-21 21:48:49.761513	\N
405	59AS8	Herman Melville	2025-05-21 21:49:07.588314	\N
406	4VI7W	Denzel Washington	2025-05-21 21:49:07.597251	\N
410	2CAFB	William Shakespeare	2025-05-21 21:49:44.64186	\N
411	19W81	Keanu Reeves	2025-05-21 21:49:44.650518	\N
412	DWZBN	Gustave Flaubert	2025-05-21 21:49:57.581295	\N
413	VLHHP	Dwayne 'The Rock' Johnson	2025-05-21 21:49:57.591718	\N
414	EDTRY	Dante Alighieri	2025-05-21 21:50:26.65298	\N
415	81MOZ	Kim Kardashian	2025-05-21 21:50:26.65595	\N
452	VAMQU	Léon Tolstoï	2025-05-21 22:06:19.31487	\N
453	761YZ	Megan Fox	2025-05-21 22:06:19.33218	\N
454	HW6X5	Roy Dupuis	2025-05-21 22:06:19.336171	\N
457	GU9YV	James Joyce	2025-05-21 22:26:04.810831	\N
458	Z5Q06	Marcel Proust	2025-05-21 22:29:12.02107	\N
\.


--
-- TOC entry 4810 (class 0 OID 66173)
-- Dependencies: 220
-- Data for Name: book; Type: TABLE DATA; Schema: bookstore; Owner: bookstoreadmin
--

COPY bookstore.book (id, code, title, date_of_publication, synopsis, number_of_pages, last_modification_date, last_modification_user) FROM stdin;
452	8BA6N	Don Quichotte	1615-12-02	Les aventures de Don Quichotte, un chevalier délirant qui décide de partir combattre le mal. L’auteur se rit des valeurs de son époque. Considéré comme le premier roman moderne.	200	2025-05-21 21:47:10.985657	\N
453	0QSOM	Cent ans de solitude	1967-01-15	L’épopée d’une famille dans une contrée d’Amérique du Sud. On traverse la fondation du village, la découverte de l’alchimie, les désastres, l’arrivée des nouveaux habitants… Un siècle d’histoire. Le roman a grandement contribué à faire connaître la littérature latino-américaine.	350	2025-05-21 21:48:34.542798	\N
454	739CO	Gatsby le Magnifique	1925-08-14	F. Scott Fitzgerald	377	2025-05-21 21:48:49.760515	\N
455	REPVR	Moby Dick	1851-07-01	Ismaël s’embarque sur un bateau et part chasser la baleine. Il fait la rencontre de Moby Dick, une terrible baleine blanche. Livre culte aux accents bibliques.	232	2025-05-21 21:49:07.586325	\N
457	2T7YC	Hamlet	1603-11-06	Le roi du Danemark vient de disparaître mystérieusement. Bientôt, son spectre apparaît à Hamlet, son fils, pour lui demander vengeance. Pièce emblématique de Shakespeare.	278	2025-05-21 21:49:44.638861	\N
458	GC4MZ	Madame Bovary	1857-02-15	Madame Bovary s’ennuie. Elle n’éprouve aucun amour pour son mari, trouve sa fille laide et s’abreuve de romans à l’eau de rose. Ces nouveaux amants pourront-ils la satisfaire? À la sortie du roman, l’auteur fut accusé pour : outrage à la morale publique et religieuse et aux bonnes mœurs.	284	2025-05-21 21:49:57.580298	\N
459	84TWJ	La Divine Comédie	1300-05-27	Immense poème écrit en tercets enchaînés, à travers l’enfer, le purgatoire et le paradis. Œuvre unique, mystique. Un des plus célèbres écrits évoquant la civilisation médiévale.	365	2025-05-21 21:50:26.650931	\N
502	J4DV4	Guerre et Paix	1869-04-22	L’invasion française de la Russie au 19e siècle et l’impact de l’ère napoléonienne sur la société tsariste, à travers l’histoire de cinq familles. D’abord publié en feuilleton dans un périodique russe entre 1865 et 1869. Tolstoï qualifiait son œuvre de « chronique historique ».	500	2025-05-21 22:06:19.312873	\N
505	A1B6V	Ulysse	1922-05-27	Une journée, en 1904, dans la vie des quelques habitants de Dublin, qui vaquent à leurs occupations. Structuré en 18 chapitres qui possèdent chacun leur style d’écriture propre. Œuvre monumentale, sorte de Mont Everest du lecteur.	300	2025-05-21 22:26:04.80982	\N
552	ED090	À la recherche du temps perdu	1927-06-21	Un narrateur se remémore ses souvenirs d’enfance et se questionne sur la mémoire, l’art et le temps. Suite romanesque monumentale de sept tomes, le deuxième livre fut primé par le Goncourt en 1919. La cathédrale de la littérature française.	400	2025-05-21 22:33:02.796458	\N
\.


--
-- TOC entry 4812 (class 0 OID 66185)
-- Dependencies: 222
-- Data for Name: book_author; Type: TABLE DATA; Schema: bookstore; Owner: bookstoreadmin
--

COPY bookstore.book_author (book_id, author_id) FROM stdin;
452	402
453	403
454	404
455	405
455	406
457	410
457	411
458	412
458	406
458	413
459	414
502	452
502	453
502	454
505	457
552	458
459	415
\.


--
-- TOC entry 4818 (class 0 OID 0)
-- Dependencies: 219
-- Name: author_id_seq; Type: SEQUENCE SET; Schema: bookstore; Owner: bookstoreadmin
--

SELECT pg_catalog.setval('bookstore.author_id_seq', 551, true);


--
-- TOC entry 4819 (class 0 OID 0)
-- Dependencies: 218
-- Name: book_id_seq; Type: SEQUENCE SET; Schema: bookstore; Owner: bookstoreadmin
--

SELECT pg_catalog.setval('bookstore.book_id_seq', 601, true);


--
-- TOC entry 4657 (class 2606 OID 66184)
-- Name: author author_pkey; Type: CONSTRAINT; Schema: bookstore; Owner: bookstoreadmin
--

ALTER TABLE ONLY bookstore.author
    ADD CONSTRAINT author_pkey PRIMARY KEY (id);


--
-- TOC entry 4660 (class 2606 OID 66201)
-- Name: book_author book_author_pkey; Type: CONSTRAINT; Schema: bookstore; Owner: bookstoreadmin
--

ALTER TABLE ONLY bookstore.book_author
    ADD CONSTRAINT book_author_pkey PRIMARY KEY (book_id, author_id);


--
-- TOC entry 4654 (class 2606 OID 66178)
-- Name: book book_pkey; Type: CONSTRAINT; Schema: bookstore; Owner: bookstoreadmin
--

ALTER TABLE ONLY bookstore.book
    ADD CONSTRAINT book_pkey PRIMARY KEY (id);


--
-- TOC entry 4658 (class 1259 OID 66199)
-- Name: idx_author_name; Type: INDEX; Schema: bookstore; Owner: bookstoreadmin
--

CREATE INDEX idx_author_name ON bookstore.author USING btree (name);


--
-- TOC entry 4655 (class 1259 OID 66198)
-- Name: idx_book_title; Type: INDEX; Schema: bookstore; Owner: bookstoreadmin
--

CREATE INDEX idx_book_title ON bookstore.book USING btree (title);


--
-- TOC entry 4661 (class 2606 OID 66217)
-- Name: book_author author_ref; Type: FK CONSTRAINT; Schema: bookstore; Owner: bookstoreadmin
--

ALTER TABLE ONLY bookstore.book_author
    ADD CONSTRAINT author_ref FOREIGN KEY (author_id) REFERENCES bookstore.author(id) NOT VALID;


--
-- TOC entry 4662 (class 2606 OID 66212)
-- Name: book_author book_ref; Type: FK CONSTRAINT; Schema: bookstore; Owner: bookstoreadmin
--

ALTER TABLE ONLY bookstore.book_author
    ADD CONSTRAINT book_ref FOREIGN KEY (book_id) REFERENCES bookstore.book(id) NOT VALID;


-- Completed on 2025-05-21 22:53:57

--
-- PostgreSQL database dump complete
--

