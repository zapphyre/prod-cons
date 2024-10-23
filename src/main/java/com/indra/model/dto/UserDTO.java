package com.indra.model.dto;

import lombok.*;

import java.util.Objects;
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

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        UserDTO userDTO = (UserDTO) o;
//        return Objects.equals(id, userDTO.id) && Objects.equals(name, userDTO.name);
//    }
//
//    @Override
//    public int hashCode() {
//        return 7;
////        return Objects.hash(id, name);
//    }
}
