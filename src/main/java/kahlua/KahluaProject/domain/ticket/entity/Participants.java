package kahlua.KahluaProject.domain.ticket.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import kahlua.KahluaProject.global.base.BaseEntity;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Participants extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="participants_id")
    private Long id;

    private String name;

    private String phone_num;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    @JsonBackReference
    private Ticket ticket;

    @Builder
    public Participants(String name, String phone_num, Ticket ticket) {
        this.name = name;
        this.phone_num = phone_num;
        this.ticket = ticket;
    }
}
