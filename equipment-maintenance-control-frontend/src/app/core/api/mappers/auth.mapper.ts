import { Address, ProfileType } from '../../../shared';
import { SessionUser } from '../../auth/models/session-user.model';
import { ApiLoginResponseDto, ViaCepResponseDto } from '../dto';

export function toAddress(dto: ViaCepResponseDto): Address {
  return {
    street: dto.logradouro,
    complement: dto.complemento,
    neighborhood: dto.bairro,
    number: 0,
    city: dto.localidade,
    state: dto.uf,
    zipCode: dto.cep,
  };
}

export function toProfileType(value: string): ProfileType {
  return value === 'EMPLOYEE' ? ProfileType.EMPLOYEE : ProfileType.CUSTOMER;
}

export function toSessionUser(dto: ApiLoginResponseDto, typedEmail: string): SessionUser {
  const email = dto.email ?? typedEmail;

  return {
    id: dto.userId,
    name: dto.name ?? email,
    email,
    profileType: toProfileType(dto.profileType),
  };
}
