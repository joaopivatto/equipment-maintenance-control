package com.web2.equipmentmaintenancecontrol.service.auth;

import com.web2.equipmentmaintenancecontrol.domain.email.Email;
import com.web2.equipmentmaintenancecontrol.domain.email.EmailProvider;
import com.web2.equipmentmaintenancecontrol.domain.email.PasswordEmail;
import com.web2.equipmentmaintenancecontrol.domain.security.HashedSaltedPassword;
import com.web2.equipmentmaintenancecontrol.domain.security.PasswordGenerator;
import com.web2.equipmentmaintenancecontrol.domain.security.PasswordHasherSalter;
import com.web2.equipmentmaintenancecontrol.exception.AppException;
import com.web2.equipmentmaintenancecontrol.exception.ErrorCode;
import com.web2.equipmentmaintenancecontrol.model.auth.dto.AddressSignupRequest;
import com.web2.equipmentmaintenancecontrol.model.auth.dto.SignupRequest;
import com.web2.equipmentmaintenancecontrol.model.profile.Address;
import com.web2.equipmentmaintenancecontrol.model.profile.Customer;
import com.web2.equipmentmaintenancecontrol.model.profile.Phone;
import com.web2.equipmentmaintenancecontrol.model.profile.ProfileType;
import com.web2.equipmentmaintenancecontrol.repository.profile.AddressRepository;
import com.web2.equipmentmaintenancecontrol.repository.profile.CustomerRepository;
import com.web2.equipmentmaintenancecontrol.repository.profile.PhoneRepository;
import com.web2.equipmentmaintenancecontrol.service.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignupService extends BaseService {
  private final CustomerRepository repository;
  private final AddressRepository addressRepository;
  private final PhoneRepository phoneRepository;
  private final PasswordGenerator passwordGenerator;
  private final PasswordHasherSalter passwordHasherSalter;
  private final EmailProvider emailProvider;
  private final PasswordEmail passwordEmail;

  private final int PASSWORD_LENGTH = 4;

  SignupService(
      CustomerRepository repository,
      AddressRepository addressRepository,
      PhoneRepository phoneRepository,
      PasswordGenerator passwordGenerator,
      PasswordHasherSalter passwordHasherSalter,
      EmailProvider emailProvider,
      PasswordEmail passwordEmail) {
    this.repository = repository;
    this.addressRepository = addressRepository;
    this.phoneRepository = phoneRepository;
    this.passwordGenerator = passwordGenerator;
    this.passwordHasherSalter = passwordHasherSalter;
    this.emailProvider = emailProvider;
    this.passwordEmail = passwordEmail;
  }

  @Transactional
  public void execute(SignupRequest request) {
    this.validateRequest(request);

    String rawPassword = this.passwordGenerator.generate(PASSWORD_LENGTH);
    HashedSaltedPassword hashedSaltedPassword = this.passwordHasherSalter.hashAndSalt(rawPassword);

    Address address = addressRepository.save(buildAddress(request.address()));
    Phone phone = phoneRepository.save(new Phone(null, request.phoneNumber()));

    Customer customer = new Customer();
    customer.setName(request.name().trim());
    customer.setEmail(request.email());
    customer.setCpf(request.cpf());
    customer.setPasswordHash(hashedSaltedPassword.hash());
    customer.setPasswordSalt(hashedSaltedPassword.salt());
    customer.setType(ProfileType.CUSTOMER);
    customer.setActive(true);
    customer.setAddress(address);
    customer.setPhone(phone);

    repository.save(customer);

    Email email = passwordEmail.build(customer.getEmail(), rawPassword);
    emailProvider.dispatch(email);
  }

  private Address buildAddress(AddressSignupRequest request) {
    return new Address(
        null,
        request.zipCode(),
        request.street(),
        request.number(),
        request.complement(),
        request.neighborhood(),
        request.city(),
        request.state());
  }

  private void validateRequest(SignupRequest request) {
    if (!isValidCpf(request.cpf())) {
      throw new AppException(ErrorCode.INVALID_CPF);
    }

    if (userAlreadyExists(request)) {
      throw new AppException(ErrorCode.CUSTOMER_ALREADY_EXISTS);
    }

    if (emailAlreadyExists(request)) {
      throw new AppException(ErrorCode.CUSTOMER_EMAIL_ALREADY_EXISTS);
    }
  }

  private boolean emailAlreadyExists(SignupRequest request) {
    return repository.findByEmail(request.email()) != null;
  }

  private boolean userAlreadyExists(SignupRequest request) {
    return repository.findByCpf(request.cpf()) != null;
  }

  private boolean isValidCpf(String cpf) {
    if (cpf == null || !cpf.matches("\\d{11}") || cpf.chars().distinct().count() == 1) {
      return false;
    }

    int firstCheckDigit = calculateCpfCheckDigit(cpf, 9);
    int secondCheckDigit = calculateCpfCheckDigit(cpf, 10);

    return firstCheckDigit == Character.getNumericValue(cpf.charAt(9))
        && secondCheckDigit == Character.getNumericValue(cpf.charAt(10));
  }

  private int calculateCpfCheckDigit(String cpf, int digitsCount) {
    int weight = digitsCount + 1;
    int sum = 0;

    for (int i = 0; i < digitsCount; i++) {
      sum += Character.getNumericValue(cpf.charAt(i)) * weight;
      weight--;
    }

    int remainder = sum % 11;
    return remainder < 2 ? 0 : 11 - remainder;
  }
}
