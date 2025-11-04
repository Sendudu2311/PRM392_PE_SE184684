# 🐛 Debug Guide - Loading Issue

## ✅ Đã thêm Logging

Code đã được thêm **chi tiết logging** để debug vấn đề "cứ xoay mãi không hiển thị data".

---

## 📋 Cách xem Logs trong Android Studio

### **Bước 1: Chạy app**
1. Click **▶ Run** (Shift+F10)
2. Chờ app khởi động

### **Bước 2: Mở Logcat**
1. Click tab **Logcat** ở bottom panel
2. Hoặc: **View** → **Tool Windows** → **Logcat**

### **Bước 3: Filter logs**

Có 3 filter tags quan trọng:

#### **Filter 1: API Logs**
```
Tag: API_LOG
```
- Hiển thị tất cả HTTP requests/responses
- Xem URL, headers, response body
- Kiểm tra API có trả về data không

#### **Filter 2: Repository Logs**
```
Tag: CourseRepository
```
- Track flow của getCourses()
- Xem số lượng courses nhận được
- Kiểm tra lỗi network hoặc parsing

#### **Filter 3: All Tags (Combo)**
```
Tag: API_LOG|CourseRepository
```
- Xem cả 2 logs cùng lúc
- Regex mode: Bật **Regex** checkbox

---

## 🔍 Các Logs sẽ thấy (Nếu thành công)

```
D/CourseRepository: 🔄 getCourses() called, forceRefresh=false
D/CourseRepository: ⏳ Emitted Loading state
D/CourseRepository: 🌐 Calling API: https://skillverse.vn/api/courses
D/API_LOG: --> GET https://skillverse.vn/api/courses?status=PUBLIC&page=0&size=100
D/API_LOG: --> END GET
D/API_LOG: <-- 200 https://skillverse.vn/api/courses?status=PUBLIC&page=0&size=100
D/API_LOG: Content-Type: application/json
D/API_LOG: {"items":[...],"page":0,"size":100,"total":125}
D/API_LOG: <-- END HTTP
D/CourseRepository: ✅ API Response received:
D/CourseRepository:    - Total items: 125
D/CourseRepository:    - Page: 0
D/CourseRepository:    - Size: 100
D/CourseRepository:    - Items count: 100
D/CourseRepository: 📘 Sample course:
D/CourseRepository:    - ID: 1
D/CourseRepository:    - Title: Introduction to Android Development
D/CourseRepository:    - Level: Beginner
D/CourseRepository:    - Thumbnail: https://...
D/CourseRepository: 🔄 Merging favorite status...
D/CourseRepository: ✅ Favorite status merged for 100 courses
D/CourseRepository: 💾 Saving to database...
D/CourseRepository: ✅ Saved 100 courses to database
D/CourseRepository: ✅ Emitted Success with 100 courses
```

---

## ❌ Các lỗi có thể gặp

### **1. Lỗi Network - Không kết nối được API**

**Logs:**
```
D/CourseRepository: ❌ Network error: UnknownHostException
D/CourseRepository:    Message: Unable to resolve host "skillverse.vn"
```

**Nguyên nhân:**
- Không có internet
- Emulator không kết nối được network
- Firewall block

**Giải pháp:**
- Check WiFi/Data
- Restart emulator
- Test API trong browser: https://skillverse.vn/api/courses?status=PUBLIC&size=10


### **2. Lỗi SSL/Certificate**

**Logs:**
```
D/CourseRepository: ❌ Network error: SSLHandshakeException
```

**Giải pháp:**
- Đổi BASE_URL từ `https://` → `http://` (nếu API support)
- Thêm `usesCleartextTraffic="true"` trong AndroidManifest (đã có rồi)


### **3. Lỗi Parsing JSON**

**Logs:**
```
D/API_LOG: <-- 200 OK
D/API_LOG: {"items": [...], ...}
D/CourseRepository: ❌ Network error: JsonSyntaxException
```

**Nguyên nhân:**
- API response structure khác với model
- Field name không khớp

**Giải pháp:**
- Check API response trong Logcat
- So sánh với Course.kt model
- Fix @SerializedName nếu cần


### **4. API trả về 0 courses**

**Logs:**
```
D/CourseRepository: ✅ API Response received:
D/CourseRepository:    - Items count: 0
D/CourseRepository: ⚠️ API returned empty list!
```

**Nguyên nhân:**
- API không có data PUBLIC
- Filter sai

**Giải pháp:**
- Test API trực tiếp: https://skillverse.vn/api/courses?status=PUBLIC
- Thử bỏ filter `status=PUBLIC`


### **5. Database error**

**Logs:**
```
D/CourseRepository: ❌ Cache error: SQLiteException
```

**Giải pháp:**
- Clear app data: Settings → Apps → TranPhamBachCat → Storage → Clear Data
- Rebuild project


---

## 🛠️ Quick Debug Checklist

### ✅ **Trước khi chạy app:**

1. **Check internet:**
   - WiFi ON
   - Emulator có network (ping google.com trong adb shell)

2. **Test API trực tiếp:**
   - Mở browser: https://skillverse.vn/api/courses?status=PUBLIC&size=10
   - Phải thấy JSON response với array courses

3. **Clean build:**
   ```
   Build → Clean Project
   Build → Rebuild Project
   ```

### ✅ **Khi app đang chạy:**

1. **Mở Logcat**
2. **Filter: `CourseRepository`**
3. **Xem log từ đầu đến cuối**
4. **Tìm icon ❌ (error)**
5. **Copy full error message → Google hoặc hỏi tôi**

---

## 📱 Test với Real API

Nếu bạn muốn test với API thật ngay bây giờ:

```bash
# Test trong terminal/PowerShell
curl "https://skillverse.vn/api/courses?status=PUBLIC&size=5"

# Hoặc trong browser
https://skillverse.vn/api/courses?status=PUBLIC&size=5
```

Nếu API trả về data → vấn đề là ở app
Nếu API không trả về → vấn đề là API hoặc network

---

## 🎯 Các điểm kiểm tra

| Vấn đề | Logcat Filter | Cần tìm |
|--------|---------------|---------|
| API có được gọi không? | `CourseRepository` | `🌐 Calling API` |
| Response code gì? | `API_LOG` | `<-- 200` hoặc `<-- 404/500` |
| Parse JSON thành công? | `CourseRepository` | `✅ API Response received` |
| Có courses không? | `CourseRepository` | `Items count: XX` |
| Lưu DB thành công? | `CourseRepository` | `✅ Saved XX courses` |
| UI nhận data? | `CourseRepository` | `✅ Emitted Success` |

---

## 💡 Nếu vẫn bị loading mãi

**Gửi cho tôi:**
1. Screenshot Logcat (filter `CourseRepository`)
2. Screenshot app (màn hình loading)
3. Copy full error message (nếu có ❌)

Tôi sẽ giúp debug cụ thể! 🚀
