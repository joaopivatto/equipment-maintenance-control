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
    validateRequest(request);

    String rawPassword = passwordGenerator.generate(PASSWORD_LENGTH);
    HashedSaltedPassword hashedSaltedPassword = passwordHasherSalter.hashAndSalt(rawPassword);

    Customer customer = buildCustomer(request, hashedSaltedPassword);
    repository.save(customer);

    sendPasswordEmail(customer, rawPassword);
  }

  private Customer buildCustomer(SignupRequest request, HashedSaltedPassword hashedSaltedPassword) {
    Address address = addressRepository.save(buildAddress(request.address()));
    Phone phone = phoneRepository.save(new Phone(null, request.phoneNumber()));

    Customer customer = new Customer();
    customer.setName(request.name());
    customer.setEmail(request.email());
    customer.setCpf(request.cpf());
    customer.setPasswordHash(hashedSaltedPassword.hash());
    customer.setPasswordSalt(hashedSaltedPassword.salt());
    customer.setType(ProfileType.CUSTOMER);
    customer.setActive(true);
    customer.setAddress(address);
    customer.setPhone(phone);

    return customer;
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

  private void sendPasswordEmail(Customer customer, String rawPassword) {
    Email email = passwordEmail.build(customer.getEmail(), rawPassword);
    emailProvider.dispatch(email);
  }

  private void validateRequest(SignupRequest request) {
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
}
