package com.chattest.entity;

import com.chattest.chat.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class User extends BaseEntity implements UserDetails {
    @Getter
    @Setter
    @Id
    private String email;

    private String password;

    @Setter
    private String name;

    @Setter
    private String profilePictureUrl; // 프로필 사진 URL 추가

    @ElementCollection(fetch= FetchType.LAZY) // 컬렉션을 위한 별도의 테이블 생성
    @Builder.Default
    private Set<UserRole> roleSet = new HashSet<>(); // Builder로 생성될 때 기본값이 HashSet



    public void addUserRole(UserRole role) {
        roleSet.add(role);
    }

    @ManyToMany
    @JoinTable(
            name="user_friends",
            joinColumns = @JoinColumn(name = "user_email"),
            inverseJoinColumns = @JoinColumn(name="friend_email")
    )
    @ToString.Exclude
    private Set<User> friends = new HashSet<>();

    public void addFriend(User friend) {
        this.friends.add(friend);
        friend.getFriends().add(this); // 양방향 관계를 위해 친구 목록에도 현재 사용자를 추가
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Spring Security 권한 관련 메소드 구현
        Set<GrantedAuthority> authorities = new HashSet<>();
        roleSet.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name())));
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
