import { ProfileType } from '../../../shared/models/base-profile.model';

export interface SessionUser {
  id: number;
  name: string;
  email: string;
  profileType: ProfileType;
}
