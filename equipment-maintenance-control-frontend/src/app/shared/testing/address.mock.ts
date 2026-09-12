import { Address } from '../models';
import { BaseMockFactory } from './mock';

export class AddressMockFactory extends BaseMockFactory<Address> {
  protected readonly length = 6;

  private readonly streets = [
    'Rua do teste',
    'Rua da simulação',
    'Rua do mock',
    'Rua do exemplo',
    'Rua do protótipo',
    'Rua do desenvolvimento',
  ];

  private readonly complements = ['Apto 101', 'Casa 202', 'Sala 303', 'Bloco B', 'Conjunto 404', 'Prédio 505'];

  private readonly neighborhoods = [
    'Bairro do teste',
    'Bairro da simulação',
    'Bairro do mock',
    'Bairro do exemplo',
    'Bairro do protótipo',
    'Bairro do desenvolvimento',
  ];

  private readonly numbers = [10, 20, 30, 40, 50, 60];

  private readonly cities = [
    'Cidade do teste',
    'Cidade da simulação',
    'Cidade do mock',
    'Cidade do exemplo',
    'Cidade do protótipo',
    'Cidade do desenvolvimento',
  ];

  private readonly states = ['SP', 'RJ', 'MG', 'RS', 'BA', 'PR'];

  private readonly zipCodes = ['12345-678', '23456-789', '34567-890', '45678-901', '56789-012', '67890-123'];

  protected build(index: number): Address {
    return {
      street: this.streets[index],
      complement: this.complements[index],
      neighborhood: this.neighborhoods[index],
      number: this.numbers[index],
      city: this.cities[index],
      state: this.states[index],
      zipCode: this.zipCodes[index],
    };
  }
}
