package org.application.entity.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.application.constant.RoleConstants;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    private String username;

    private String email;

    private String password;

    private RoleConstants role;

}
