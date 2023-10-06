# CalendarDiaryApp
나만의 일기장

# 사용 기술
- Kotlin
- MVVM
- Coroutine
- ViewBinding
- Koin


# 💡 Topic

- 캘린더를 기반으로 사진과 함께 나의 일상생활을 기록할 수 있는 앱

# 🤔 Learned

- MVVM 패턴 기반 설계 (AAC에서 권장하는 패턴 중 Usecase 패턴 미사용)
  - [데이팅 앱](https://github.com/tkdgns8234/DatingApp)에서 usecase 패턴을 사용했으나, 비즈니스로직을 usecase로 추가로 분리해야하는 비용이 적지 않다고 생각되어 usecase를 제외한 MVVM으로 구현
- Repository 패턴으로 데이터 로직을 추상화 및 재사용 가능하도록 구현
- Activity, ViewMdoel의 BaseClass 를 만들어 반복되는 코드의 양을 줄이고 일관성을 위해 앱의 전반적인 구현 틀을 만들어 개발 진행


# ⭐️ Key Function

- 앱 최초 실행 시 나오는 첫 화면으로 오늘 날짜에 맞는 달력을 보여준다
- 캘린더의 주말 및 공휴일 표시 기능 구현
    - [국가 공휴일 API 링크](https://date.nager.at/Api)
- 일기를 작성한 날짜는 점으로 표시
- 캘린더 월 이동 기능
- 날짜 선택 시, 화면 하단에 일기 미리 보기 표출
- settings 버튼을 누를 시, 한국어/영어 언어 선택 가능, 선택시 즉시 언어 변환
