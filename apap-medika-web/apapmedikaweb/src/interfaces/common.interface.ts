export interface CommonResponseInterface<T> {
    data: T,
    message: String,
    status: number,
    timestamp: Date
}