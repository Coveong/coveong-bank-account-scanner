# Coveong Account Scanner
코봉 계좌 스캐너 - 카메라로 계좌 인식

## Features
### 사진촬영 & 인식
- 계좌가 있는 부분을 촬영하면 OCR을 활용해 계좌를 인식합니다.
- 인식 이후에는 자동으로 복사가 되기 때문에 간편하게 붙여넣기가 가능합니다.

### 송급 앱 연결
- 인식 이후에 타 송금 앱을 바로 열 수 있도록 송금 앱 연결 기능을 지원합니다.
  
## Screenshot
![image](https://user-images.githubusercontent.com/42836576/103003041-6439d780-4573-11eb-83b1-fe288bcb3839.png)

## Implements
- [Google Vision Api](https://cloud.google.com/vision) : OCR 필기 인식
- [Retrofit2](https://github.com/square/retrofit) : HTTP 통신
- [Camera2](https://developer.android.com/reference/android/hardware/camera2/package-summary) : 커스텀 카메라 인터페이스 구축
- [Junit4](https://github.com/junit-team/junit4) : 테스트 코드 작성

## Contributing
1. [Fork it](https://github.com/Coveong/coveong-bank-account-scanner/fork)
2. Create your feature branch (git checkout -b feature/description-#issue-number)
3. Commit your changes (git commit -am 'Add some fooBar')
4. Push to the branch (git push origin feature/description-#issue-number)
5. Create a new Pull Request

## License
This project is licensed under the MIT License

```
Copyright 2020 Coveong


Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
