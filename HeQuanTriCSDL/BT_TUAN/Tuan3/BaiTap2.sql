USE QLKhachSan

-- 2. Tạo Trigger tính tiền và điền tự động vào bảng tDoanhThu như sau:
-- Các trường lấy thông tin từ các bảng và các thông tin sau:
-- Trong đó:
-- (a) Số Ngày Ở= Ngày Ra – Ngày Vào
-- (b) ThucThu: Tính theo yêu cầu sau:
-- Nếu Số Ngày ở <10 Thành tiền = Đơn Giá * Số ngày ở
-- Nếu 10 <=Số Ngày ở <30 Thành Tiền = Đơn Giá* Số Ngày ở * 0.95 (Giảm5%)
-- Nếu Số ngày ở >= 30 Thành Tiền = Đơn Giá* Số Ngày ở * 0.9 (Giảm10%)

CREATE TRIGGER trg_1
ON tDoanhThu
AFTER INSERT, UPDATE
AS
BEGIN
	DECLARE @SoNgayO INT
	SELECT @SoNgayO = DATEDIFF(DAY, NgayVao, NgayRa)
	FROM tDangKy dk 
	INNER JOIN inserted i ON i.MaDK = dk.MaDK

	UPDATE dt
	SET 
		SoNgayO = @SoNgayO,
		ThucThu = CASE
			WHEN @SoNgayO < 10 THEN lp.DonGia * @SoNgayO 
			WHEN @SoNgayO < 30 THEN lp.DonGia * @SoNgayO * 0.95
			ELSE lp.DonGia * @SoNgayO * 0.9
		END	
	FROM tDoanhThu dt
	INNER JOIN inserted i ON dt.MaDK = i.MaDK
	INNER JOIN tDangKy dk ON dk.MaDK = dt.MaDK
	INNER JOIN tLoaiPhong lp ON lp.LoaiPhong = dk.LoaiPhong
END