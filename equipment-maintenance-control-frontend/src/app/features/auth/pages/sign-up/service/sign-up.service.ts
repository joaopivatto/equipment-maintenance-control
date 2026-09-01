import { Address } from '../../../../../shared';

export class SignUpService {
  constructor(
    private readonly apiClient: any, // Replace with the actual type of the backend client // Replace with the actual type of the viacep client
  ) {}

  // PLACEHOLDER: Implement the sign-up logic here, including validation and API calls
  public async signUp(request: any): Promise<void> {
    if (!this.emailIsValid(request.email, request.confirmEmail)) {
      throw new Error('Emails do not match');
    }

    const address = await this.getAddressByZipCode(request.address.zipCode);

    try {
      await this.apiClient.post('/sign-up', {
        ...request,
        address,
      });
    } catch (error) {
      throw new Error('Failed to sign up');
    }
  }

  private async getAddressByZipCode(zipCode: string): Promise<void> {}

  private emailIsValid(email: string, confirmEmail: string): boolean {
    return email === confirmEmail;
  }

  private cpfIsValid(cpf: string): boolean {
    return true;
  }
}
