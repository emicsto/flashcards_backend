INSERT INTO users (id, email, name)
VALUES ('1', 'invoices.mail.app@gmail.com', 'user');

INSERT INTO flashcards (id, deck_id, user_id, front, back)
VALUES (1, 1, 1, 'Front #1', 'Back #1'),
       (2, 1, 1, 'Front #2', 'Back #2'),
       (3, 1, 1, 'Front #3', 'Back #3'),
       (4, 2, 1, 'Front #4', 'Back #4'),
       (5, 2, 1, 'Front #5', 'Back #5'),
       (6, 2, 1, 'Front #6', 'Back #6'),
       (7, 2, 1, 'Front #7', 'Back #7');

INSERT INTO decks (id, user_id, name)
VALUES (1, 1, 'Deck #1'),
       (2, 1, 'Deck #2'),
       (3, 1, 'Deck #3'),
       (4, 1, 'Deck #4'),
       (5, 1, 'Deck #5'),
       (6, 1, 'Deck #6'),
       (7, 1, 'Deck #7');
