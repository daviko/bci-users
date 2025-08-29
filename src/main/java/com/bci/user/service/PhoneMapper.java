package com.bci.user.service;

import com.bci.user.dto.PhoneRequest;
import com.bci.user.dto.PhoneResponse;
import com.bci.user.model.Phone;
import java.util.List;
import java.util.stream.Collectors;

public class PhoneMapper {

  public static List<Phone> toEntities(List<PhoneRequest> phoneRequests) {
    if (phoneRequests == null) return null;

    return phoneRequests.stream().map(PhoneMapper::toEntity).collect(Collectors.toList());
  }

  public static Phone toEntity(PhoneRequest phoneRequest) {
    Phone phone = new Phone();
    phone.setNumber(phoneRequest.getNumber());
    phone.setCityCode(phoneRequest.getCityCode());
    phone.setCountryCode(phoneRequest.getCountryCode());
    return phone;
  }

  public static List<PhoneResponse> toResponses(List<Phone> phones) {
    if (phones == null) return null;

    return phones.stream().map(PhoneMapper::toResponse).collect(Collectors.toList());
  }

  public static PhoneResponse toResponse(Phone phone) {
    return new PhoneResponse(phone.getNumber(), phone.getCityCode(), phone.getCountryCode());
  }
}
