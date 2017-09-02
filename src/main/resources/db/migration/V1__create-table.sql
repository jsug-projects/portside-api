CREATE TABLE attendee (
  id    BINARY(16) NOT NULL,
  email VARCHAR(255),
  PRIMARY KEY (id)
);

CREATE TABLE session (
  id          BINARY(16)   NOT NULL,
  description VARCHAR(255),
  speaker     VARCHAR(255),
  title       VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE session_attendee (
  session_id  BINARY(16) NOT NULL,
  attendee_id BINARY(16) NOT NULL
);

CREATE TABLE session_speaker (
  session_id BINARY(16) NOT NULL,
  speaker_id BINARY(16) NOT NULL
);

CREATE TABLE speaker (
  id        BINARY(16) NOT NULL,
  belong_to VARCHAR(255),
  image     BLOB,
  name      VARCHAR(255)
                       NOT NULL,
  profile   VARCHAR(255),
  PRIMARY KEY (id)
);

ALTER TABLE session_attendee
  ADD CONSTRAINT FKphkmg7y8i9gmi9nnmynyrvk0q FOREIGN KEY (attendee_id)
REFERENCES attendee (id);

ALTER TABLE session_attendee
  ADD CONSTRAINT FK9d9hkqfvlyumyjmr1ojl567cn FOREIGN KEY (session_id)
REFERENCES session (id);

ALTER TABLE session_speaker
  ADD CONSTRAINT FKhrn3hw2mt276jhmi5wm6or7e FOREIGN KEY (speaker_id)
REFERENCES speaker (id);

ALTER TABLE session_speaker
  ADD CONSTRAINT FK3isa2jjqf19hbbprc9elpjbcn FOREIGN KEY (session_id)
REFERENCES session (id);
