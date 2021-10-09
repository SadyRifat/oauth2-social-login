package com.oauth2.social.login.entity;

import com.oauth2.social.login.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "role", schema = "jwt_spring_security_db")
public class Role {
	@Id
	@Column(name = "id")
	private String id;

	@Enumerated(EnumType.STRING)
	@Column(name = "name")
	private ERole name;
}
