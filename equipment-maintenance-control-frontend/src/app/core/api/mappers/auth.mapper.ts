import { Address } from '../../../shared';
import { ViaCepResponseDto } from '../dto';

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
