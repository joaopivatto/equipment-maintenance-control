import { ProfileType } from '../../../../shared';
import { BaseMockFactory } from '../../../../shared/testing';
import { SessionUser } from '../session-user.model';

export class MockSessionUserFactory extends BaseMockFactory<SessionUser & { password: string }> {
  protected readonly length = 2;
  private readonly names = ['João da Silva', 'Maria da Costa'];

  private readonly emails = ['joao@cliente.com', 'maria@empresa.com'];

  private readonly passwords = ['1234', '4321'];

  private readonly profileTypes = [ProfileType.CUSTOMER, ProfileType.EMPLOYEE];

  protected build(index: number): SessionUser & { password: string } {
    return {
      id: index + 1,
      name: this.names[index],
      email: this.emails[index],
      profileType: this.profileTypes[index],
      password: this.passwords[index],
    };
  }
}
