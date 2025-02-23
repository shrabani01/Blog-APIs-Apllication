package com.blogApplication.app.payloads;

import java.util.HashSet;
import java.util.Set;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDao {
   
	private int id;
    @NotEmpty
    @Size(min = 6 , message="Name should be atleast 6 characters!!")
	private String name;
    @Email(message = "Email address is not valid!!")
	private String email;
    @NotEmpty
    @Size(min = 4,max = 18 , message="Password should be minimum 4 and maximum 18 characters!!")
	private String password;
    @NotEmpty
	private String about;

    private Set<RoleDao> roles = new HashSet<>();
}
