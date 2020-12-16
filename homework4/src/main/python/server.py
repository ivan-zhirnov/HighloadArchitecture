from concurrent import futures
import grpc
import PhoneService_pb2
import PhoneService_pb2_grpc
import time


class PhoneService(PhoneService_pb2_grpc.PhoneService):
    def checkPhone(self, request, context):
        response = PhoneService_pb2.PhoneResponse()
        response.responseCode, response.message = check_phone(request.phone, request.code)
        return response


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=4))
    PhoneService_pb2_grpc.add_PhoneServiceServicer_to_server(PhoneService(), server)

    print('Start server on port 8081')
    server.add_insecure_port('[::]:8081')
    server.start()

    try:
        while True:
            time.sleep(8640)
    except KeyboardInterrupt:
        server.stop(0)


def check_phone(phone, code):
    phone_map = {'123': '123',
                 '234': '234',
                 '345': '345'}

    if phone in list(phone_map):
        if phone_map.get(phone) == code:
            return 0, "Phone and code are correct"
        else:
            return 1, "Code is incorrect"
    else:
        return 2, "Phone doesn`t exist"


if __name__ == '__main__':
    serve()
