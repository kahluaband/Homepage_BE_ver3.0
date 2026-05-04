package kahlua.KahluaProject.domain.user.service;

import kahlua.KahluaProject.domain.ticket.repository.ParticipantsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipantsService {

    private final ParticipantsRepository participantsRepository;

    public Integer getTotalGeneralTicket(Long ticketId) {

        return participantsRepository.countByTicket_Id(ticketId) + 1;
    }
}
