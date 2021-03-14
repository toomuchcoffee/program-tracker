ALTER TABLE pt_user ADD PRIMARY KEY (username);
ALTER TABLE pt_user
   ADD CONSTRAINT fk_client_to_coach
   FOREIGN KEY (coach_id)
   REFERENCES pt_user(username);