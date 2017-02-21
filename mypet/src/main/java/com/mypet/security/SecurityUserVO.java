package com.mypet.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.mypet.domain.MemberVO;

public class SecurityUserVO implements UserDetails {	
	private MemberVO vo;
	private UserRole role;

	
	public SecurityUserVO(MemberVO vo) {
		this.vo = vo;	
		this.role = new UserRole("ROLE_"+vo.getRole());
	}	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(this.role);
		return authorities;
	}

	@Override
	public String getPassword() {
		return vo.getUser_password();
	}

	@Override
	public String getUsername() {
		return vo.getUser_id();
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}
	
	

}
