import { ProfileType } from '../../../shared/models/user';

export interface SessionUser {
  token?: string;
  id: number;
  name: string;
  email: string;
  profileType: ProfileType;
}
