import { CreateUserDto } from './generated/dto';

export class CreateUser {
    static type = '[UM] CreateUser';
    constructor(public readonly payload: CreateUserDto) { }
}
