export interface ServerError {
    code: Number;
    message: String;
    details: { [index: string]: Array<String> };
}
