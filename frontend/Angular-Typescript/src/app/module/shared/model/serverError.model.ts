export interface ServerError {
    code: Number;
    message: String;
    details: Map<String, Array<String>>;
}
