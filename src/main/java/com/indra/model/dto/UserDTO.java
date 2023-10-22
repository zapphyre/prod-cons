package com.indra.model.dto;

import lombok.*;

import java.util.UUID;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserDTO {
    private Long id;
    private UUID uuid;
    private String name;
}
