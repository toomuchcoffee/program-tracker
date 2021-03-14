CREATE TABLE program (
    client_id VARCHAR(255),
    coach_id VARCHAR(255),
    notes TEXT,
    PRIMARY KEY (client_id, coach_id),
    CONSTRAINT fk_program_to_client
          FOREIGN KEY(client_id)
    	  REFERENCES pt_user(username),
    CONSTRAINT fk_program_to_coach
              FOREIGN KEY(coach_id)
        	  REFERENCES pt_user(username)
);