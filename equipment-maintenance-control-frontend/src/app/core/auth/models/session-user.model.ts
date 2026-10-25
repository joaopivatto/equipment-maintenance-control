import { ProfileType } from '../../../shared/models/user/base-profile.model';

export interface SessionUser {
  token?: string;
  id: number;
  name: string;
  email: string;
  profileType: ProfileType;
}
