INSERT INTO users (user_id, email, first_name, last_name, password, telegram_id)
VALUES (1,'user@gmail.com','User','Bob','$2a$10$nlvpUzuDGQ/Iu2ethysvLec1.Dj6YxJeaqcAeDtTodfqqT7uUcgja', null);

INSERT INTO roles (role_id, role)
VALUES (1, 'USER');

INSERT INTO user_role (user_id, role_id)
VALUES (1, 1);

INSERT INTO addresses (address_id, country, city, street, number_build, postcode)
VALUES (1,'Ukraine','Kyiv','Shevchenko',1,'01011');

INSERT INTO addresses (address_id, country, city, street, number_build, postcode)
VALUES (2,'Ukraine','Kyiv','Shevchenko',1,'01011');

INSERT INTO amenities (amenities_id, name, description)
VALUES (1,'shower','hot and cold water');

INSERT INTO accommodation (accommodation_id, type, address_id, size, daily_rate, availability)
VALUES (1,'HOUSE',1,'Studio',5.5,10);

INSERT INTO accommodation (accommodation_id, type, address_id, size, daily_rate, availability)
VALUES (4,'APARTMENT',2, 'Bedroom',10.00,100);

INSERT INTO accommodation_amenities (accommodation_id, amenities_id)
VALUES (1,1);

INSERT INTO accommodation_amenities (accommodation_id, amenities_id)
VALUES (4,1);

INSERT INTO bookings (booking_id, in_date, out_date, accommodation_id, user_id, status, version, is_deleted)
VALUES (1,'2024-03-30','2024-04-01',1,1,'PAID',1,false);

INSERT INTO bookings (booking_id, in_date, out_date, accommodation_id, user_id, status, version, is_deleted)
VALUES (3,'2024-11-04','2024-11-05',4,1,'PAID',1,false);

INSERT INTO payments (payment_id, payment_status, booking_id, session_url, session_id, amount, version, is_deleted)
VALUES (1,'PAID',1,'https://checkout.stripe.com/c/pay/cs_test_a1aI4GJhMQCyjjdJqEkewuPH4hFQ4I61ubxfhmQ8BpR3BT8Ls5R3la797T#fidkdWxOYHwnPyd1blpxYHZxWjA0VTdGfGlGYUpUQndiR2Z2Sn08MmhmUjA8Z2c9TWlgd0Zpc39fNGtwQkRtQ0ZGZk1DaE48NnBuaHdOcDE9X3ViVGxRNjZRdkRyQmhhd2E0QG1tMV1Uf2R3NTV8Tj11UElxTScpJ2N3amhWYHdzYHcnP3F3cGApJ2lkfGpwcVF8dWAnPyd2bGtiaWBabHFgaCcpJ2BrZGdpYFVpZGZgbWppYWB3dic%2FcXdwYHgl','cs_test_a1aI4GJhMQCyjjdJqEkewuPH4hFQ4I61ubxfhmQ8BpR3BT8Ls5R3la797T',11.00,1,false);

INSERT INTO payments (payment_id, payment_status, booking_id, session_url, session_id, amount, version, is_deleted)
VALUES (2,'PAID',3,'https://checkout.stripe.com/c/pay/cs_test_a13Z7Y91IcQWQIdJGZWFgq8csNPTbNe69guR8hbHKGWTEGNV2FXQ7wRSj1#fidkdWxOYHwnPyd1blpxYHZxWjA0VTdGfGlGYUpUQndiR2Z2Sn08MmhmUjA8Z2c9TWlgd0Zpc39fNGtwQkRtQ0ZGZk1DaE48NnBuaHdOcDE9X3ViVGxRNjZRdkRyQmhhd2E0QG1tMV1Uf2R3NTV8Tj11UElxTScpJ2N3amhWYHdzYHcnP3F3cGApJ2lkfGpwcVF8dWAnPyd2bGtiaWBabHFgaCcpJ2BrZGdpYFVpZGZgbWppYWB3dic%2FcXdwYHgl','cs_test_a13Z7Y91IcQWQIdJGZWFgq8csNPTbNe69guR8hbHKGWTEGNV2FXQ7wRSj1',10.00,1,false);
