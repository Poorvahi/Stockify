--
-- PostgreSQL database dump
--

\restrict h6rfRxrxfvVlOd9OsDR6Nbep5qYgGaV9JJkznq1NnzFhR0ZS9EJo0cMfCO8wIq3

-- Dumped from database version 18.1
-- Dumped by pg_dump version 18.1

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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: comments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.comments (
    id bigint NOT NULL,
    content character varying(255) NOT NULL,
    module_id bigint NOT NULL,
    module_type character varying(255) NOT NULL,
    created_at timestamp(6) without time zone,
    username character varying(255)
);


ALTER TABLE public.comments OWNER TO postgres;

--
-- Name: comments_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.comments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.comments_id_seq OWNER TO postgres;

--
-- Name: comments_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.comments_id_seq OWNED BY public.comments.id;


--
-- Name: notifications; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.notifications (
    id bigint NOT NULL,
    message character varying(255) NOT NULL,
    created_at timestamp(6) without time zone,
    username character varying(255) NOT NULL
);


ALTER TABLE public.notifications OWNER TO postgres;

--
-- Name: notifications_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.notifications_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.notifications_id_seq OWNER TO postgres;

--
-- Name: notifications_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.notifications_id_seq OWNED BY public.notifications.id;


--
-- Name: password_reset_otp; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.password_reset_otp (
    id bigint NOT NULL,
    created_at timestamp(6) without time zone,
    email character varying(255) NOT NULL,
    expires_at timestamp(6) without time zone NOT NULL,
    otp_hash character varying(255) NOT NULL,
    is_verified boolean
);


ALTER TABLE public.password_reset_otp OWNER TO postgres;

--
-- Name: password_reset_otp_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.password_reset_otp_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.password_reset_otp_id_seq OWNER TO postgres;

--
-- Name: password_reset_otp_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.password_reset_otp_id_seq OWNED BY public.password_reset_otp.id;


--
-- Name: product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product (
    product_id bigint NOT NULL,
    created_on timestamp(6) without time zone,
    is_deleted boolean,
    modified_on timestamp(6) without time zone,
    product_description character varying(200),
    product_name character varying(100),
    product_price double precision DEFAULT 0.0,
    product_quantity integer,
    warehouse_id bigint
);


ALTER TABLE public.product OWNER TO postgres;

--
-- Name: product_product_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.product_product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.product_product_id_seq OWNER TO postgres;

--
-- Name: product_product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.product_product_id_seq OWNED BY public.product.product_id;


--
-- Name: purchase; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.purchase (
    purchase_id bigint NOT NULL,
    created_on timestamp(6) without time zone,
    is_deleted boolean DEFAULT false,
    modified_on timestamp(6) without time zone,
    price double precision DEFAULT 0.0,
    quantity double precision DEFAULT 0.0,
    total double precision DEFAULT 0.0,
    product_id bigint NOT NULL
);


ALTER TABLE public.purchase OWNER TO postgres;

--
-- Name: purchase_purchase_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.purchase_purchase_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.purchase_purchase_id_seq OWNER TO postgres;

--
-- Name: purchase_purchase_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.purchase_purchase_id_seq OWNED BY public.purchase.purchase_id;


--
-- Name: sales; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sales (
    sales_id bigint NOT NULL,
    created_on timestamp(6) without time zone,
    is_deleted boolean DEFAULT false,
    modified_on timestamp(6) without time zone,
    price double precision DEFAULT 0.0,
    quantity double precision DEFAULT 0.0,
    total double precision DEFAULT 0.0,
    product_id bigint NOT NULL
);


ALTER TABLE public.sales OWNER TO postgres;

--
-- Name: sales_sales_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sales_sales_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sales_sales_id_seq OWNER TO postgres;

--
-- Name: sales_sales_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sales_sales_id_seq OWNED BY public.sales.sales_id;


--
-- Name: stock_movement; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.stock_movement (
    id bigint NOT NULL,
    created_on timestamp(6) without time zone,
    movement_type character varying(10) NOT NULL,
    quantity double precision NOT NULL,
    reference_id bigint,
    reference_type character varying(20),
    product_id bigint NOT NULL,
    warehouse_id bigint NOT NULL
);


ALTER TABLE public.stock_movement OWNER TO postgres;

--
-- Name: stock_movement_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.stock_movement_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.stock_movement_id_seq OWNER TO postgres;

--
-- Name: stock_movement_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.stock_movement_id_seq OWNED BY public.stock_movement.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    password character varying(255),
    role character varying(255),
    username character varying(255),
    email character varying(255),
    full_name character varying(255)
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: warehouse; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.warehouse (
    warehouse_id bigint NOT NULL,
    created_on timestamp(6) without time zone,
    is_deleted boolean DEFAULT false,
    modified_on timestamp(6) without time zone,
    warehouse_code character varying(50),
    warehouse_name character varying(100)
);


ALTER TABLE public.warehouse OWNER TO postgres;

--
-- Name: warehouse_warehouse_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.warehouse_warehouse_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.warehouse_warehouse_id_seq OWNER TO postgres;

--
-- Name: warehouse_warehouse_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.warehouse_warehouse_id_seq OWNED BY public.warehouse.warehouse_id;


--
-- Name: comments id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments ALTER COLUMN id SET DEFAULT nextval('public.comments_id_seq'::regclass);


--
-- Name: notifications id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notifications ALTER COLUMN id SET DEFAULT nextval('public.notifications_id_seq'::regclass);


--
-- Name: password_reset_otp id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.password_reset_otp ALTER COLUMN id SET DEFAULT nextval('public.password_reset_otp_id_seq'::regclass);


--
-- Name: product product_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product ALTER COLUMN product_id SET DEFAULT nextval('public.product_product_id_seq'::regclass);


--
-- Name: purchase purchase_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase ALTER COLUMN purchase_id SET DEFAULT nextval('public.purchase_purchase_id_seq'::regclass);


--
-- Name: sales sales_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales ALTER COLUMN sales_id SET DEFAULT nextval('public.sales_sales_id_seq'::regclass);


--
-- Name: stock_movement id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock_movement ALTER COLUMN id SET DEFAULT nextval('public.stock_movement_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Name: warehouse warehouse_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.warehouse ALTER COLUMN warehouse_id SET DEFAULT nextval('public.warehouse_warehouse_id_seq'::regclass);


--
-- Data for Name: comments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.comments (id, content, module_id, module_type, created_at, username) FROM stdin;
1	@poorvahi stock verified	3	PRODUCT	2026-04-10 10:18:14.388964	poorvahi
2	@poorvahi vbmn,	7	PRODUCT	2026-04-10 16:57:28.004203	poorvahi
\.


--
-- Data for Name: notifications; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.notifications (id, message, created_at, username) FROM stdin;
\.


--
-- Data for Name: password_reset_otp; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.password_reset_otp (id, created_at, email, expires_at, otp_hash, is_verified) FROM stdin;
1	2026-04-10 11:25:51.01284	poorvakadwe@gmail.com	2026-04-10 11:35:51.01284	$2a$10$Zk6U.h5QgGqXfs4D95RGiO4C6ddDa1aajZMxg9lyV/5qe6nBK3sJC	f
2	2026-04-18 15:26:43.107463	poorvahikadwe@gmail.com	2026-04-18 15:36:43.108468	$2a$10$qIaHyGvoIcilsOOWSP0H.usXu14dZQzwymLazC37pNmU5cBChd/qO	f
3	2026-04-18 15:26:45.522397	poorvahikadwe@gmail.com	2026-04-18 15:36:45.522397	$2a$10$bkK5i1.edA2pQzeE14XRWeZj0fjHY5vS4CtsD6EWvDq7ADbV4yv4a	f
4	2026-04-18 15:26:46.310636	poorvahikadwe@gmail.com	2026-04-18 15:36:46.310636	$2a$10$g3QLXzSm0hKBmF.1v32OFeM6jAKkp2F1cqiJZPc1/WdvI0rWYoZuq	t
\.


--
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.product (product_id, created_on, is_deleted, modified_on, product_description, product_name, product_price, product_quantity, warehouse_id) FROM stdin;
2	2026-04-10 10:02:16.556071	f	2026-04-10 10:02:16.556071	Electronics 2m fast-charge cable	USB-C Cable	12.5	200	1
4	2026-04-10 10:02:16.562068	f	2026-04-10 10:02:16.563066	Stationery blue ink multipack	Ballpoint Pens (10)	6	150	2
10	2026-04-10 10:02:16.58212	f	2026-04-10 10:02:16.58212	Stationery metal 40 sheet	Stapler Heavy Duty	11.49	55	2
1	2026-04-10 10:02:16.54507	f	2026-04-10 10:02:16.627669	Electronics compact ergonomic mouse	Wireless Mouse	29.99	127	1
5	2026-04-10 10:02:16.565066	f	2026-04-10 10:02:16.65559	Groceries basmati long grain	Rice 5kg	18.99	65	3
6	2026-04-10 10:02:16.570118	f	2026-04-10 10:02:16.657588	Groceries sunflower refined	Cooking Oil 1L	8.5	48	3
8	2026-04-10 10:02:16.576682	f	2026-04-10 10:02:16.659588	Furniture mesh back ergonomic	Office Chair	189	15	2
3	2026-04-10 10:02:16.559075	f	2026-04-10 10:56:24.723544	Stationery ruled 200 pages	Notebook A5	4.25	5	2
9	2026-04-10 10:02:16.579689	t	2026-04-10 10:58:29.511781	Electronics alkaline long life	AA Batteries (8)	9.99	90	1
7	2026-04-10 10:02:16.573179	f	2026-04-10 12:38:53.90106	Furniture adjustable warm white	Desk Lamp LED	42	90	2
11	2026-04-18 14:44:31.411114	f	2026-04-18 14:44:31.412162		Hair Accessories	10000	20	\N
12	2026-04-18 14:44:31.531095	f	2026-04-18 14:44:31.531095	Dell	Mouse	1000	100	\N
13	2026-04-18 14:44:31.534089	f	2026-04-18 14:44:31.534089	Bata	Footware	2000	50	\N
14	2026-04-18 14:44:31.536089	f	2026-04-18 14:44:31.536089	JBL	Headphones	2500	50	\N
15	2026-04-18 14:44:31.539091	f	2026-04-18 14:44:31.539091	Gucci	Hand Bags	40000	10	\N
\.


--
-- Data for Name: purchase; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.purchase (purchase_id, created_on, is_deleted, modified_on, price, quantity, total, product_id) FROM stdin;
1	2026-04-10 10:02:16.586616	f	2026-04-10 10:02:16.586616	29.99	15	449.84999999999997	1
2	2026-04-10 10:02:16.596618	f	2026-04-10 10:02:16.597618	18.99	20	379.79999999999995	5
3	2026-04-10 10:02:16.600616	f	2026-04-10 10:02:16.601614	189	3	567	8
4	2026-04-10 10:07:38.075693	f	2026-04-10 10:07:38.076704	4.25	7	29.75	3
5	2026-04-10 10:55:46.982254	f	2026-04-10 10:55:46.982254	4.25	62	263.5	3
6	2026-04-10 12:38:53.876097	f	2026-04-10 12:38:53.876097	42	55	2310	7
\.


--
-- Data for Name: sales; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sales (sales_id, created_on, is_deleted, modified_on, price, quantity, total, product_id) FROM stdin;
1	2026-04-10 10:02:16.605762	f	2026-04-10 10:02:16.605762	29.99	8	239.92	1
2	2026-04-10 10:02:16.611762	f	2026-04-10 10:02:16.611762	4.25	25	106.25	3
3	2026-04-10 10:02:16.615018	f	2026-04-10 10:02:16.615018	8.5	12	102	6
4	2026-04-10 10:56:24.732116	f	2026-04-10 10:56:24.732116	4.25	119	505.75	3
\.


--
-- Data for Name: stock_movement; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.stock_movement (id, created_on, movement_type, quantity, reference_id, reference_type, product_id, warehouse_id) FROM stdin;
1	2026-04-10 10:07:38.128896	IN	7	4	PURCHASE	3	2
2	2026-04-10 10:55:47.013329	IN	62	5	PURCHASE	3	2
3	2026-04-10 10:56:24.738943	OUT	119	4	SALES	3	2
4	2026-04-10 12:38:53.907082	IN	55	6	PURCHASE	7	2
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, password, role, username, email, full_name) FROM stdin;
2	$2a$10$1oV8FZPEcH.StLVObvPt3eG7Q5qsimhOI43lHGhMnTijORYvhYhvK	ROLE_ADMIN	admin	admin@stockify.local	System Administrator
3	$2a$10$omnrPCZKxQNDG1U5ahD8seuMco2IVWrf9GOJTgOHmdxAUcgTC9ea6	ROLE_USER	clerk	clerk@stockify.local	Inventory Clerk
1	$2a$10$bRSCu/RCZx42KdXZHY7m9OJhE3TYQzQK8iSSsYR0trC4WJYdB1dNa	ROLE_ADMIN	poorvahi	poorvakadwe@gmail.com	poorvahi
4	$2a$10$hLcAxk0aAjAr9IEhDcQAS.3BBwPmU.g.aCCTM7HXAsT1d.y4m9rqW	ROLE_USER	user	user@dmart.org	user
5	$2a$10$Lx1CdkQQn2hCtIwAhODdN.017JXRG2kryHfJ/FEarC4qWqFBO8lbS	ROLE_USER	user1	user@dmart.com	User
6	$2a$10$.7EYxucwScbWaoZsellt9O9Ehb5gZU25EfySlhfnmqlN/5C.XdVmG	ROLE_USER	poorva	poorvahikadwe@gmail.com	k
\.


--
-- Data for Name: warehouse; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.warehouse (warehouse_id, created_on, is_deleted, modified_on, warehouse_code, warehouse_name) FROM stdin;
1	2026-04-10 10:02:16.526349	f	2026-04-10 10:02:16.526349	WH-N-01	North Hub
2	2026-04-10 10:02:16.535703	f	2026-04-10 10:02:16.535703	WH-S-02	South Depot
3	2026-04-10 10:02:16.539715	f	2026-04-10 10:02:16.539715	WH-C-03	Central DC
\.


--
-- Name: comments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.comments_id_seq', 2, true);


--
-- Name: notifications_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.notifications_id_seq', 2, true);


--
-- Name: password_reset_otp_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.password_reset_otp_id_seq', 4, true);


--
-- Name: product_product_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.product_product_id_seq', 15, true);


--
-- Name: purchase_purchase_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.purchase_purchase_id_seq', 6, true);


--
-- Name: sales_sales_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sales_sales_id_seq', 4, true);


--
-- Name: stock_movement_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.stock_movement_id_seq', 4, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 6, true);


--
-- Name: warehouse_warehouse_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.warehouse_warehouse_id_seq', 3, true);


--
-- Name: comments comments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_pkey PRIMARY KEY (id);


--
-- Name: notifications notifications_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notifications
    ADD CONSTRAINT notifications_pkey PRIMARY KEY (id);


--
-- Name: password_reset_otp password_reset_otp_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.password_reset_otp
    ADD CONSTRAINT password_reset_otp_pkey PRIMARY KEY (id);


--
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (product_id);


--
-- Name: purchase purchase_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase
    ADD CONSTRAINT purchase_pkey PRIMARY KEY (purchase_id);


--
-- Name: sales sales_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales
    ADD CONSTRAINT sales_pkey PRIMARY KEY (sales_id);


--
-- Name: stock_movement stock_movement_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock_movement
    ADD CONSTRAINT stock_movement_pkey PRIMARY KEY (id);


--
-- Name: users uk_6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: warehouse warehouse_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.warehouse
    ADD CONSTRAINT warehouse_pkey PRIMARY KEY (warehouse_id);


--
-- Name: purchase fk3s4jktret4nl7m8yhfc8mfrn5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchase
    ADD CONSTRAINT fk3s4jktret4nl7m8yhfc8mfrn5 FOREIGN KEY (product_id) REFERENCES public.product(product_id);


--
-- Name: sales fkfcs4mjmgry6xchs16dv03eclp; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales
    ADD CONSTRAINT fkfcs4mjmgry6xchs16dv03eclp FOREIGN KEY (product_id) REFERENCES public.product(product_id);


--
-- Name: product fkk6edvfdkq61mjhltx856ncs3x; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT fkk6edvfdkq61mjhltx856ncs3x FOREIGN KEY (warehouse_id) REFERENCES public.warehouse(warehouse_id);


--
-- Name: stock_movement fkmp40immc6bpap7qpthgr3ff2g; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock_movement
    ADD CONSTRAINT fkmp40immc6bpap7qpthgr3ff2g FOREIGN KEY (warehouse_id) REFERENCES public.warehouse(warehouse_id);


--
-- Name: stock_movement fkq63e7y5l2pnh2tt2lvxlquvbf; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock_movement
    ADD CONSTRAINT fkq63e7y5l2pnh2tt2lvxlquvbf FOREIGN KEY (product_id) REFERENCES public.product(product_id);


--
-- PostgreSQL database dump complete
--

\unrestrict h6rfRxrxfvVlOd9OsDR6Nbep5qYgGaV9JJkznq1NnzFhR0ZS9EJo0cMfCO8wIq3

