package de.toomuchcoffee.pt.service;

import de.toomuchcoffee.pt.domain.entity.*;
import de.toomuchcoffee.pt.domain.repository.ProgramRepository;
import de.toomuchcoffee.pt.domain.repository.UserRepository;
import de.toomuchcoffee.pt.dto.ProgramDto;
import de.toomuchcoffee.pt.dto.UpdateUserDto;
import de.toomuchcoffee.pt.mapper.ProgramMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class CoachService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private ProgramMapper programMapper;

    public List<String> findAvailableClients() {
        return userRepository.findAll().stream()
                .filter(u -> u instanceof Client)
                .map(Client.class::cast)
                .filter(client -> client.getCoach() == null)
                .map(Client::getUsername)
                .collect(toList());
    }

    public List<String> findAssignedClientsForCoach(String coachUsername) {
        return userRepository.findById(coachUsername)
                .filter(coach -> coach instanceof Coach)
                .map(Coach.class::cast)
                .map(Coach::getClients)
                .map(clients -> clients.stream()
                        .map(User::getUsername)
                        .collect(toList()))
                .orElse(new ArrayList<>());
    }

    public void addClient(UpdateUserDto coach, String clientUsername) {
        userRepository.findById(coach.getUsername())
                .map(Coach.class::cast)
                .ifPresentOrElse(c -> userRepository.findById(clientUsername)
                        .map(Client.class::cast)
                        .ifPresent(u -> {
                            u.setCoach(c);
                            userRepository.save(u);
                        }), () -> {
                    throw new IllegalArgumentException("Could not find existing user with username " + coach.getUsername());
                });
    }

    public void removeClient(UpdateUserDto coach, String clientUsername) {
        userRepository.findById(coach.getUsername())
                .map(Coach.class::cast)
                .ifPresentOrElse(c -> {
                    userRepository.findById(clientUsername)
                            .map(Client.class::cast)
                            .ifPresent(client -> {
                                client.setCoach(null);
                                userRepository.save(client);
                            });
                }, () -> {
                    throw new IllegalArgumentException("Could not find existing user with username " + coach.getUsername());
                });
    }

    public void saveProgram(ProgramDto programDto) {
        Program program = programMapper.toEntity(programDto);
        programRepository.save(program);
    }

    public ProgramDto retrieveProgram(String coachUsername, String clientUsername) {
        return programRepository.findById(new ProgramId(coachUsername, clientUsername))
                .map(program -> programMapper.toDto(program))
                .orElse(ProgramDto.builder().coach(coachUsername).client(clientUsername).build());
    }
}
