package com.books.bonnets.librarian.mapper;

import com.books.bonnets.librarian.dto.LoginDto;
import com.books.bonnets.librarian.dto.UserDto;
import com.books.bonnets.librarian.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    //User Mapper
    UserDto toDto(User user);
    User toEntity(UserDto userDto);
    //Login Mapper
    LoginDto toLoginDto(User user);
    User toEntity(LoginDto loginDto);
}
