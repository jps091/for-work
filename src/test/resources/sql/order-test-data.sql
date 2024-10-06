-- 4 : 70000 , 5 : 70000 6 : 65000 , 7 : 30000, 8 : 99000

-- 4 결제완료
INSERT INTO `orders` (`ordered_at`, `order_id`, `user_id`, `status`, `total_amount`, `request_id`)
VALUES ('2024-09-04 00:18:39', '1', '1', 'ORDER', '70000', 'test-request-id-1');

-- 6결제완료 7 부분취소
INSERT INTO `orders` (`canceled_at`, `ordered_at`, `order_id`, `user_id`, `status`, `total_amount`, `request_id`)
VALUES ('2024-09-09 08:28:39', '2024-09-09 08:18:39', '2', '2', 'PARTIAL_CANCEL', '65000','test-request-id-2');

-- 7, 8 구매확정
INSERT INTO `orders` (`confirmed_at`, `ordered_at`, `order_id`, `user_id`, `status`, `total_amount`, `request_id`)
VALUES ('2024-09-05 14:55:39', '2024-09-05 15:20:39', '3', '3', 'CONFIRM', '129000', 'test-request-id-3');

-- 5 발송대기
INSERT INTO `orders` (`ordered_at`, `order_id`, `user_id`, `status`, `total_amount`, `request_id`)
VALUES ('2024-08-14 18:18:39', '4', '4', 'WAIT', '70000', 'test-request-id-4');

-- 7 발송대기 5 취소
INSERT INTO `orders` (`canceled_at`, `ordered_at`, `order_id`, `user_id`, `status`, `total_amount`, `request_id`)
VALUES ('2024-09-11 09:23:39', '2024-09-11 09:18:39', '5', '1', 'PARTIAL_WAIT', '30000','test-request-id-5');

-- 전체 취소
INSERT INTO `orders` (`canceled_at`, `ordered_at`, `order_id`, `user_id`, `status`, `total_amount`, `request_id`)
VALUES ('2024-09-05 00:21:39', '2024-09-05 00:18:39', '6', '1', 'CANCEL', '0','test-request-id-6');

-- 5,6,7 결제완료
INSERT INTO `orders` (`ordered_at`, `order_id`, `user_id`, `status`, `total_amount`, `request_id`)
VALUES ('2024-09-04 00:18:39', '7', '2', 'ORDER', '165000','test-request-id-7');

-- 4 취소 6 부분구매확정
INSERT INTO `orders` (`confirmed_at`, `canceled_at`, `ordered_at`, `order_id`, `user_id`, `status`, `total_amount`, `request_id`)
VALUES ('2024-09-09 08:38:39', '2024-09-09 08:28:39', '2024-09-09 08:18:39', '8', '3', 'PARTIAL_CONFIRM', '65000','test-request-id-8');