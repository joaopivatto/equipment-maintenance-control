import { Customer, ProfileType } from '../models';
import { AddressMockFactory } from './address.mock';
import { BaseMockFactory } from './mock';

export class CustomerMockFactory extends BaseMockFactory<Customer> {
  protected readonly length = 10;

  private readonly addressFactory = new AddressMockFactory();

  private readonly names = [
    'João da Silva',
    'José de Souza',
    'Joana Pereira',
    'Joaquina Maria',
    'Testerson de Testes',
    'Exempleton da Simulação',
    'Mockson do Mock',
    'Prototypeiro do Protótipo',
    'Desenvolvimentinho do Desenvolvimento',
    'Simulador de Simulações',
  ];

  private readonly emails = [
    'joao.silva@example.com',
    'jose.souza@example.com',
    'joana.pereira@example.com',
    'joaquina.maria@example.com',
    'testerson.testes@example.com',
    'exemplo.da.simulacao@example.com',
    'mockson.do.mock@example.com',
    'prototypeiro.do.prototipo@example.com',
    'desenvolvimentinho.do.desenvolvimento@example.com',
    'simulador.de.simulacoes@example.com',
  ];

  private readonly cpfNumbers = [
    '123.456.789-00',
    '234.567.890-11',
    '345.678.901-22',
    '456.789.012-33',
    '123.456.789-01',
    '234.567.890-12',
    '345.678.901-23',
    '456.789.012-34',
    '567.890.123-45',
    '678.901.234-56',
  ];

  private readonly phoneNumbers = [
    '(11) 91234-5678',
    '(21) 92345-6789',
    '(31) 93456-7890',
    '(41) 94567-8901',
    '(11) 91234-5678',
    '(21) 92345-6789',
    '(31) 93456-7890',
    '(41) 94567-8901',
    '(51) 95678-9012',
    '(61) 96789-0123',
  ];

  protected build(index: number): Customer {
    return {
      name: this.names[index],
      email: this.emails[index],
      address: this.addressFactory.generate(),
      cpf: this.cpfNumbers[index],
      phoneNumber: this.phoneNumbers[index],
      profileType: ProfileType.CUSTOMER,
    };
  }
}
