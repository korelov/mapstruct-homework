package org.javaacademy.mapstruct_homework.mapper;

import org.javaacademy.mapstruct_homework.dto.PersonCreditDto;
import org.javaacademy.mapstruct_homework.dto.PersonDriverLicenceDto;
import org.javaacademy.mapstruct_homework.dto.PersonInsuranceDto;
import org.javaacademy.mapstruct_homework.entity.Address;
import org.javaacademy.mapstruct_homework.entity.Human;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

@Mapper
public interface HumanMapper {

    @Mapping(target = "passportNumber", source = ".", qualifiedByName = "getPassportNumber")
    @Mapping(target = "salary", source = ".", qualifiedByName = "getSalary")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fullAddress", source = ".", qualifiedByName = "getFullAddress")
    PersonCreditDto convertToCreditDto(Human human);

    @Named("getPassportNumber")
    default String getPassportNumber(Human human) {
        return "%s %s".formatted(
                human.getPassport().getSeries(),
                human.getPassport().getNumber());
    }

    @Named("getSalary")
    default String getSalary(Human human) {
        return "%s %s".formatted(
                human.getWork().getSalary(),
                human.getWork().getCurrency().toString());
    }

    @Named("getFullAddress")
    default String getFullAddress(Human human) {
        Address livingAddress = human.getLivingAddress();

        String fullAddress = getValue(livingAddress.getRegion()) +
                " " +
                getValue(livingAddress.getCity()) +
                " " +
                getValue(livingAddress.getStreet()) +
                " " +
                getValue(livingAddress.getHouse()) +
                " " +
                getValue(livingAddress.getFlat());

        return fullAddress.trim();
    }

    private String getValue(String value) {
        return value != null ? value : "";
    }

    @Mapping(target = "fullName", source = ".", qualifiedByName = "getFullName")
    @Mapping(target = "fullPassportData", source = ".", qualifiedByName = "getFullPassportData")
    @Mapping(target = "birthDate", source = ".", qualifiedByName = "getBirthDate")
    PersonDriverLicenceDto convertToDriverLicenceDto(Human human);

    @Named("getFullName")
    default String getFullName(Human human) {
        return "%s %s %s".formatted(
                human.getFirstName(),
                human.getLastName(),
                human.getMiddleName()).trim();
    }

    @Named("getFullPassportData")
    default String getFullPassportData(Human human) {
        String seriesNumber = getPassportNumber(human).replaceAll(" ", "");
        return "%s %s".formatted(
                seriesNumber,
                human.getPassport().getIssueDate().format(DateTimeFormatter.ofPattern("d.M.yyyy")));
    }

    @Named("getBirthDate")
    default String getBirthDate(Human human) {
        return "%s.%s.%s".formatted(
                human.getBirthDay(),
                human.getBirthMonth(),
                human.getBirthYear());
    }

    @Mapping(target = "fullName", source = ".", qualifiedByName = "getFullName")
    @Mapping(target = "fullAddress", source = ".", qualifiedByName = "getFullAddress")
    @Mapping(target = "fullAge", source = ".", qualifiedByName = "getFullAge")
    PersonInsuranceDto convertToInsuranceDto(Human human);

    @Named("getFullAge")
    default Integer getFullAge(Human human) {
        LocalDate humanDateOfBirth = LocalDate.of(
                human.getBirthYear(),
                human.getBirthMonth(),
                human.getBirthDay());
        return Period.between(humanDateOfBirth, LocalDate.now()).getYears();
    }
}
