package com.buge.player.ui

import com.buge.player.data.AppLanguage

/** All application-facing copy is kept here so every language selector entry changes real UI text. */
class AppText(locale: AppLanguage) {
    private data class Copy(
        val home: String, val library: String, val settings: String, val nowPlaying: String,
        val addStream: String, val streamHint: String, val title: String, val artist: String,
        val artwork: String, val mediaType: String, val playNow: String, val cancel: String,
        val recent: String, val forYou: String, val randomMediaHint: String, val favorites: String,
        val deviceMedia: String, val scanDevice: String, val scanning: String, val emptyLibrary: String,
        val noRecent: String, val noFavorites: String, val theme: String, val accent: String,
        val language: String, val keepScreenOn: String, val autoplay: String, val system: String,
        val light: String, val dark: String, val artworkColor: String, val violet: String,
        val ocean: String, val sunset: String, val forest: String, val queue: String,
        val clearQueue: String, val speed: String, val permissionNeeded: String, val playerReady: String,
        val unableToPlay: String, val audio: String, val video: String, val automatic: String,
        val dynamicNote: String, val welcomeEyebrow: String, val welcomeTitle: String,
        val welcomeBody: String, val welcomeStart: String, val welcomeFeatureOne: String,
        val welcomeFeatureTwo: String
    )

    private val en = Copy(
        home = "Home", library = "Library", settings = "Settings", nowPlaying = "Now playing",
        addStream = "Add network media", streamHint = "HTTP, HTTPS, or HLS (.m3u8) URL",
        title = "Title", artist = "Artist / channel", artwork = "Artwork image URL (optional)",
        mediaType = "Media type", playNow = "Play now", cancel = "Cancel",
        recent = "Recently played", forYou = "Random picks for you", randomMediaHint = "Tap any card to play",
        favorites = "Favorites", deviceMedia = "On this device", scanDevice = "Scan device media",
        scanning = "Scanning…", emptyLibrary = "No device media found",
        noRecent = "Played media will appear here.", noFavorites = "Tap a heart to add media here.",
        theme = "App theme", accent = "Color source", language = "Language",
        keepScreenOn = "Keep screen on while playing", autoplay = "Autoplay", system = "System",
        light = "Light", dark = "Dark", artworkColor = "Dynamic artwork color", violet = "Violet",
        ocean = "Ocean", sunset = "Sunset", forest = "Forest", queue = "Queue", clearQueue = "Clear queue",
        speed = "Playback speed", permissionNeeded = "Media permission is required to scan device media.",
        playerReady = "Ready to play", unableToPlay = "Unable to play this media", audio = "Audio",
        video = "Video", automatic = "Automatic",
        dynamicNote = "This extracts a real dominant color from embedded art, video frames, or the supplied artwork URL.",
        welcomeEyebrow = "YOUR SOUND SPACE", welcomeTitle = "Every frame,\ndeserves a soundtrack.",
        welcomeBody = "Play device media, HTTP audio and video, and HLS live streams—then let the interface bloom from what you are watching.",
        welcomeStart = "Begin listening", welcomeFeatureOne = "Seamless streaming", welcomeFeatureTwo = "Color, made personal"
    )

    private val copy = when (locale) {
        AppLanguage.ENGLISH -> en
        AppLanguage.FRENCH -> en.copy(
            home = "Accueil", library = "Bibliothèque", settings = "Paramètres", nowPlaying = "En cours",
            addStream = "Ajouter un média réseau", streamHint = "URL HTTP, HTTPS ou HLS (.m3u8)",
            title = "Titre", artist = "Artiste / chaîne", artwork = "URL de l’image de pochette (facultatif)",
            mediaType = "Type de média", playNow = "Lire", cancel = "Annuler", recent = "Écoutés récemment",
            forYou = "Suggestions aléatoires", randomMediaHint = "Touchez une carte pour lire",
            favorites = "Favoris", deviceMedia = "Sur cet appareil", scanDevice = "Analyser les médias de l’appareil",
            scanning = "Analyse…", emptyLibrary = "Aucun média trouvé sur l’appareil",
            noRecent = "Les médias lus apparaîtront ici.", noFavorites = "Touchez un cœur pour ajouter un média ici.",
            theme = "Thème de l’application", accent = "Source de couleur", language = "Langue",
            keepScreenOn = "Garder l’écran allumé pendant la lecture", autoplay = "Lecture automatique",
            system = "Système", light = "Clair", dark = "Sombre", artworkColor = "Couleur dynamique de la pochette",
            violet = "Violet", ocean = "Océan", sunset = "Coucher de soleil", forest = "Forêt",
            queue = "File de lecture", clearQueue = "Vider la file", speed = "Vitesse de lecture",
            permissionNeeded = "L’autorisation des médias est requise pour analyser l’appareil.",
            playerReady = "Prêt à lire", unableToPlay = "Impossible de lire ce média", audio = "Audio", video = "Vidéo",
            automatic = "Automatique", dynamicNote = "Extrait une couleur dominante réelle de la pochette intégrée, des images vidéo ou de l’URL de pochette fournie.",
            welcomeEyebrow = "VOTRE ESPACE SONORE", welcomeTitle = "Chaque image\nmérite une bande-son.",
            welcomeBody = "Lisez les médias de l’appareil, l’audio et la vidéo HTTP, ainsi que les flux HLS en direct, puis laissez l’interface s’inspirer de ce que vous regardez.",
            welcomeStart = "Commencer à écouter", welcomeFeatureOne = "Streaming fluide", welcomeFeatureTwo = "La couleur, personnalisée"
        )
        AppLanguage.GERMAN -> en.copy(
            home = "Startseite", library = "Mediathek", settings = "Einstellungen", nowPlaying = "Wiedergabe",
            addStream = "Netzwerkmedium hinzufügen", streamHint = "HTTP-, HTTPS- oder HLS-URL (.m3u8)",
            title = "Titel", artist = "Künstler / Kanal", artwork = "Coverbild-URL (optional)",
            mediaType = "Medientyp", playNow = "Jetzt abspielen", cancel = "Abbrechen", recent = "Zuletzt gespielt",
            forYou = "Zufällige Empfehlungen", randomMediaHint = "Tippe auf eine Karte zum Abspielen",
            favorites = "Favoriten", deviceMedia = "Auf diesem Gerät", scanDevice = "Gerätemedien scannen",
            scanning = "Wird gescannt…", emptyLibrary = "Keine Gerätemedien gefunden",
            noRecent = "Abgespielte Medien werden hier angezeigt.", noFavorites = "Tippe auf ein Herz, um Medien hier hinzuzufügen.",
            theme = "App-Design", accent = "Farbquelle", language = "Sprache",
            keepScreenOn = "Bildschirm während der Wiedergabe eingeschaltet lassen", autoplay = "Automatische Wiedergabe",
            system = "System", light = "Hell", dark = "Dunkel", artworkColor = "Dynamische Coverfarbe",
            violet = "Violett", ocean = "Ozean", sunset = "Sonnenuntergang", forest = "Wald",
            queue = "Warteschlange", clearQueue = "Warteschlange leeren", speed = "Wiedergabegeschwindigkeit",
            permissionNeeded = "Zum Scannen von Gerätemedien ist eine Medienberechtigung erforderlich.",
            playerReady = "Bereit zur Wiedergabe", unableToPlay = "Dieses Medium kann nicht wiedergegeben werden", audio = "Audio", video = "Video",
            automatic = "Automatisch", dynamicNote = "Extrahiert eine echte dominante Farbe aus eingebettetem Cover, Videobildern oder der angegebenen Cover-URL.",
            welcomeEyebrow = "DEIN KLANGRAUM", welcomeTitle = "Jedes Bild\nverdient einen Soundtrack.",
            welcomeBody = "Spiele Gerätemedien, HTTP-Audio und -Video sowie HLS-Livestreams ab und lass die Oberfläche aus dem wachsen, was du ansiehst.",
            welcomeStart = "Jetzt hören", welcomeFeatureOne = "Nahtloses Streaming", welcomeFeatureTwo = "Farbe, persönlich"
        )
        AppLanguage.RUSSIAN -> en.copy(
            home = "Главная", library = "Медиатека", settings = "Настройки", nowPlaying = "Сейчас играет",
            addStream = "Добавить сетевой медиафайл", streamHint = "URL HTTP, HTTPS или HLS (.m3u8)",
            title = "Название", artist = "Исполнитель / канал", artwork = "URL обложки (необязательно)",
            mediaType = "Тип медиа", playNow = "Воспроизвести", cancel = "Отмена", recent = "Недавно воспроизведённые",
            forYou = "Случайные рекомендации", randomMediaHint = "Нажмите карточку для воспроизведения",
            favorites = "Избранное", deviceMedia = "На этом устройстве", scanDevice = "Сканировать медиа устройства",
            scanning = "Сканирование…", emptyLibrary = "Медиа на устройстве не найдены",
            noRecent = "Воспроизведённые медиа появятся здесь.", noFavorites = "Нажмите сердечко, чтобы добавить медиа сюда.",
            theme = "Тема приложения", accent = "Источник цвета", language = "Язык",
            keepScreenOn = "Не выключать экран во время воспроизведения", autoplay = "Автовоспроизведение",
            system = "Система", light = "Светлая", dark = "Тёмная", artworkColor = "Динамический цвет обложки",
            violet = "Фиолетовый", ocean = "Океан", sunset = "Закат", forest = "Лес",
            queue = "Очередь", clearQueue = "Очистить очередь", speed = "Скорость воспроизведения",
            permissionNeeded = "Для сканирования медиа устройства требуется разрешение.",
            playerReady = "Готово к воспроизведению", unableToPlay = "Не удалось воспроизвести этот медиафайл", audio = "Аудио", video = "Видео",
            automatic = "Автоматически", dynamicNote = "Извлекает реальный доминирующий цвет из встроенной обложки, кадров видео или указанного URL обложки.",
            welcomeEyebrow = "ВАШЕ ЗВУКОВОЕ ПРОСТРАНСТВО", welcomeTitle = "Каждый кадр\nзаслуживает саундтрека.",
            welcomeBody = "Воспроизводите медиа устройства, HTTP-аудио и видео, а также HLS-трансляции — и позвольте интерфейсу расцвести от того, что вы смотрите.",
            welcomeStart = "Начать слушать", welcomeFeatureOne = "Плавный стриминг", welcomeFeatureTwo = "Цвет по-вашему"
        )
        AppLanguage.PORTUGUESE -> en.copy(
            home = "Início", library = "Biblioteca", settings = "Definições", nowPlaying = "A reproduzir",
            addStream = "Adicionar média de rede", streamHint = "URL HTTP, HTTPS ou HLS (.m3u8)",
            title = "Título", artist = "Artista / canal", artwork = "URL da capa (opcional)",
            mediaType = "Tipo de média", playNow = "Reproduzir agora", cancel = "Cancelar", recent = "Reproduzidos recentemente",
            forYou = "Sugestões aleatórias", randomMediaHint = "Toque num cartão para reproduzir",
            favorites = "Favoritos", deviceMedia = "Neste dispositivo", scanDevice = "Pesquisar multimédia do dispositivo",
            scanning = "A pesquisar…", emptyLibrary = "Nenhum ficheiro multimédia encontrado",
            noRecent = "Os conteúdos reproduzidos aparecerão aqui.", noFavorites = "Toque num coração para adicionar conteúdos aqui.",
            theme = "Tema da aplicação", accent = "Origem da cor", language = "Idioma",
            keepScreenOn = "Manter o ecrã ligado durante a reprodução", autoplay = "Reprodução automática",
            system = "Sistema", light = "Claro", dark = "Escuro", artworkColor = "Cor dinâmica da capa",
            violet = "Violeta", ocean = "Oceano", sunset = "Pôr do sol", forest = "Floresta",
            queue = "Fila", clearQueue = "Limpar fila", speed = "Velocidade de reprodução",
            permissionNeeded = "É necessária permissão de multimédia para pesquisar o dispositivo.",
            playerReady = "Pronto a reproduzir", unableToPlay = "Não foi possível reproduzir este conteúdo", audio = "Áudio", video = "Vídeo",
            automatic = "Automático", dynamicNote = "Extrai uma cor dominante real da capa incorporada, dos fotogramas de vídeo ou do URL de capa fornecido.",
            welcomeEyebrow = "O SEU ESPAÇO SONORO", welcomeTitle = "Cada imagem\nmerece uma banda sonora.",
            welcomeBody = "Reproduza ficheiros do dispositivo, áudio e vídeo HTTP e transmissões HLS em direto, deixando a interface florescer a partir do que vê.",
            welcomeStart = "Começar a ouvir", welcomeFeatureOne = "Streaming contínuo", welcomeFeatureTwo = "Cor personalizada"
        )
        AppLanguage.PORTUGUESE_BRAZIL -> en.copy(
            home = "Início", library = "Biblioteca", settings = "Configurações", nowPlaying = "Tocando agora",
            addStream = "Adicionar mídia de rede", streamHint = "URL HTTP, HTTPS ou HLS (.m3u8)",
            title = "Título", artist = "Artista / canal", artwork = "URL da capa (opcional)",
            mediaType = "Tipo de mídia", playNow = "Tocar agora", cancel = "Cancelar", recent = "Tocados recentemente",
            forYou = "Escolhas aleatórias para você", randomMediaHint = "Toque em qualquer cartão para tocar",
            favorites = "Favoritos", deviceMedia = "Neste dispositivo", scanDevice = "Escanear mídia do dispositivo",
            scanning = "Escaneando…", emptyLibrary = "Nenhuma mídia encontrada no dispositivo",
            noRecent = "As mídias reproduzidas aparecerão aqui.", noFavorites = "Toque no coração para adicionar mídias aqui.",
            theme = "Tema do aplicativo", accent = "Fonte de cor", language = "Idioma",
            keepScreenOn = "Manter a tela ligada durante a reprodução", autoplay = "Reprodução automática",
            system = "Sistema", light = "Claro", dark = "Escuro", artworkColor = "Cor dinâmica da capa",
            violet = "Violeta", ocean = "Oceano", sunset = "Pôr do sol", forest = "Floresta",
            queue = "Fila", clearQueue = "Limpar fila", speed = "Velocidade de reprodução",
            permissionNeeded = "É necessária permissão de mídia para escanear o dispositivo.",
            playerReady = "Pronto para tocar", unableToPlay = "Não foi possível tocar esta mídia", audio = "Áudio", video = "Vídeo",
            automatic = "Automático", dynamicNote = "Extrai uma cor dominante real da capa incorporada, de quadros de vídeo ou da URL de capa fornecida.",
            welcomeEyebrow = "SEU ESPAÇO SONORO", welcomeTitle = "Cada quadro\nmerece uma trilha sonora.",
            welcomeBody = "Toque mídias do dispositivo, áudio e vídeo HTTP e transmissões HLS ao vivo, deixando a interface florescer com o que você está assistindo.",
            welcomeStart = "Começar a ouvir", welcomeFeatureOne = "Streaming sem interrupções", welcomeFeatureTwo = "Cor do seu jeito"
        )
        AppLanguage.SPANISH -> en.copy(
            home = "Inicio", library = "Biblioteca", settings = "Ajustes", nowPlaying = "Reproduciendo",
            addStream = "Añadir medio de red", streamHint = "URL HTTP, HTTPS o HLS (.m3u8)",
            title = "Título", artist = "Artista / canal", artwork = "URL de portada (opcional)",
            mediaType = "Tipo de medio", playNow = "Reproducir ahora", cancel = "Cancelar", recent = "Reproducidos recientemente",
            forYou = "Sugerencias aleatorias", randomMediaHint = "Toca cualquier tarjeta para reproducir",
            favorites = "Favoritos", deviceMedia = "En este dispositivo", scanDevice = "Buscar medios del dispositivo",
            scanning = "Buscando…", emptyLibrary = "No se encontraron medios en el dispositivo",
            noRecent = "Los medios reproducidos aparecerán aquí.", noFavorites = "Toca un corazón para añadir medios aquí.",
            theme = "Tema de la aplicación", accent = "Fuente de color", language = "Idioma",
            keepScreenOn = "Mantener la pantalla encendida durante la reproducción", autoplay = "Reproducción automática",
            system = "Sistema", light = "Claro", dark = "Oscuro", artworkColor = "Color dinámico de portada",
            violet = "Violeta", ocean = "Océano", sunset = "Atardecer", forest = "Bosque",
            queue = "Cola", clearQueue = "Vaciar cola", speed = "Velocidad de reproducción",
            permissionNeeded = "Se requiere permiso de medios para buscar en el dispositivo.",
            playerReady = "Listo para reproducir", unableToPlay = "No se puede reproducir este medio", audio = "Audio", video = "Vídeo",
            automatic = "Automático", dynamicNote = "Extrae un color dominante real de la portada incrustada, de fotogramas de vídeo o de la URL de portada indicada.",
            welcomeEyebrow = "TU ESPACIO SONORO", welcomeTitle = "Cada fotograma\nmerece una banda sonora.",
            welcomeBody = "Reproduce medios del dispositivo, audio y vídeo HTTP y transmisiones HLS en directo, y deja que la interfaz florezca con lo que estás viendo.",
            welcomeStart = "Empezar a escuchar", welcomeFeatureOne = "Streaming fluido", welcomeFeatureTwo = "Color personal"
        )
        AppLanguage.CHINESE -> en.copy(
            home = "主页", library = "媒体库", settings = "设置", nowPlaying = "正在播放",
            addStream = "添加网络媒体", streamHint = "HTTP、HTTPS 或 HLS (.m3u8) 地址",
            title = "标题", artist = "艺术家 / 频道", artwork = "封面图片 URL（可选）", mediaType = "媒体类型",
            playNow = "立即播放", cancel = "取消", recent = "最近播放", forYou = "为你随机推荐", randomMediaHint = "轻触任意卡片即可播放",
            favorites = "收藏", deviceMedia = "设备媒体", scanDevice = "扫描设备媒体", scanning = "正在扫描…",
            emptyLibrary = "尚未找到设备媒体", noRecent = "播放媒体后，最近项目会出现在这里。", noFavorites = "点按心形图标，将媒体加入收藏。",
            theme = "界面主题", accent = "配色来源", language = "语言", keepScreenOn = "播放时保持屏幕常亮", autoplay = "自动播放",
            system = "跟随系统", light = "浅色", dark = "深色", artworkColor = "媒体封面动态取色", violet = "紫罗兰",
            ocean = "海洋", sunset = "日落", forest = "森林", queue = "播放队列", clearQueue = "清空队列", speed = "播放速度",
            permissionNeeded = "需要媒体访问权限才能扫描设备媒体。", playerReady = "已准备就绪", unableToPlay = "无法播放此媒体",
            audio = "音频", video = "视频", automatic = "自动", dynamicNote = "此模式会从嵌入封面、视频画面或填写的封面 URL 提取主色。",
            welcomeEyebrow = "你的声音空间", welcomeTitle = "每一帧，\n都值得聆听。",
            welcomeBody = "播放设备媒体、HTTP 音视频与 HLS 直播流。让色彩从你正在欣赏的画面中自然生长。",
            welcomeStart = "开始聆听", welcomeFeatureOne = "无缝网络播放", welcomeFeatureTwo = "属于你的动态配色"
        )
        AppLanguage.CHINESE_TRADITIONAL -> en.copy(
            home = "首頁", library = "媒體庫", settings = "設定", nowPlaying = "正在播放",
            addStream = "加入網路媒體", streamHint = "HTTP、HTTPS 或 HLS (.m3u8) 位址",
            title = "標題", artist = "演出者 / 頻道", artwork = "封面圖片 URL（選填）", mediaType = "媒體類型",
            playNow = "立即播放", cancel = "取消", recent = "最近播放", forYou = "為你隨機推薦", randomMediaHint = "輕觸任一張卡片即可播放",
            favorites = "收藏", deviceMedia = "裝置媒體", scanDevice = "掃描裝置媒體", scanning = "正在掃描…",
            emptyLibrary = "尚未找到裝置媒體", noRecent = "播放過的媒體將顯示在這裡。", noFavorites = "點選愛心即可將媒體加入收藏。",
            theme = "應用程式主題", accent = "色彩來源", language = "語言", keepScreenOn = "播放時保持螢幕開啟", autoplay = "自動播放",
            system = "跟隨系統", light = "淺色", dark = "深色", artworkColor = "媒體封面動態取色", violet = "紫羅蘭",
            ocean = "海洋", sunset = "日落", forest = "森林", queue = "播放佇列", clearQueue = "清空佇列", speed = "播放速度",
            permissionNeeded = "需要媒體存取權限才能掃描裝置媒體。", playerReady = "已準備就緒", unableToPlay = "無法播放此媒體",
            audio = "音訊", video = "影片", automatic = "自動", dynamicNote = "此模式會從內嵌封面、影片畫面或提供的封面 URL 擷取主要色彩。",
            welcomeEyebrow = "你的聲音空間", welcomeTitle = "每一幀，\n都值得聆聽。",
            welcomeBody = "播放裝置媒體、HTTP 音訊與影片和 HLS 直播串流，讓色彩從你正在欣賞的畫面自然生長。",
            welcomeStart = "開始聆聽", welcomeFeatureOne = "無縫網路播放", welcomeFeatureTwo = "專屬動態配色"
        )
        AppLanguage.ARABIC -> en.copy(
            home = "الرئيسية", library = "المكتبة", settings = "الإعدادات", nowPlaying = "قيد التشغيل",
            addStream = "إضافة وسائط شبكة", streamHint = "عنوان HTTP أو HTTPS أو HLS (.m3u8)",
            title = "العنوان", artist = "الفنان / القناة", artwork = "رابط صورة الغلاف (اختياري)",
            mediaType = "نوع الوسائط", playNow = "تشغيل الآن", cancel = "إلغاء", recent = "تم تشغيله مؤخراً",
            forYou = "اختيارات عشوائية لك", randomMediaHint = "اضغط على أي بطاقة للتشغيل",
            favorites = "المفضلة", deviceMedia = "على هذا الجهاز", scanDevice = "فحص وسائط الجهاز",
            scanning = "جارٍ الفحص…", emptyLibrary = "لم يتم العثور على وسائط على الجهاز",
            noRecent = "ستظهر الوسائط التي تم تشغيلها هنا.", noFavorites = "اضغط على القلب لإضافة الوسائط هنا.",
            theme = "مظهر التطبيق", accent = "مصدر اللون", language = "اللغة",
            keepScreenOn = "إبقاء الشاشة قيد التشغيل أثناء التشغيل", autoplay = "تشغيل تلقائي",
            system = "النظام", light = "فاتح", dark = "داكن", artworkColor = "لون الغلاف الديناميكي",
            violet = "بنفسجي", ocean = "محيط", sunset = "غروب", forest = "غابة",
            queue = "قائمة التشغيل", clearQueue = "مسح القائمة", speed = "سرعة التشغيل",
            permissionNeeded = "إذن الوسائط مطلوب لفحص وسائط الجهاز.",
            playerReady = "جاهز للتشغيل", unableToPlay = "تعذر تشغيل هذه الوسائط", audio = "صوت", video = "فيديو",
            automatic = "تلقائي", dynamicNote = "يستخرج لوناً سائداً حقيقياً من الغلاف المضمن أو إطارات الفيديو أو رابط الغلاف المقدم.",
            welcomeEyebrow = "مساحتك الصوتية", welcomeTitle = "كل إطار\nيستحق موسيقى تصويرية.",
            welcomeBody = "شغّل وسائط الجهاز والصوت والفيديو عبر HTTP والبث المباشر HLS، ودع الواجهة تزدهر بما تشاهده.",
            welcomeStart = "ابدأ الاستماع", welcomeFeatureOne = "بث سلس", welcomeFeatureTwo = "لون بطابعك"
        )
        AppLanguage.JAPANESE -> en.copy(
            home = "ホーム", library = "ライブラリ", settings = "設定", nowPlaying = "再生中",
            addStream = "ネットワークメディアを追加", streamHint = "HTTP、HTTPS、または HLS (.m3u8) URL",
            title = "タイトル", artist = "アーティスト / チャンネル", artwork = "アートワーク画像 URL（任意）",
            mediaType = "メディアの種類", playNow = "今すぐ再生", cancel = "キャンセル", recent = "最近再生した項目",
            forYou = "ランダムおすすめ", randomMediaHint = "カードをタップして再生",
            favorites = "お気に入り", deviceMedia = "このデバイス", scanDevice = "デバイスのメディアをスキャン",
            scanning = "スキャン中…", emptyLibrary = "デバイスにメディアが見つかりません",
            noRecent = "再生したメディアがここに表示されます。", noFavorites = "ハートをタップしてメディアを追加します。",
            theme = "アプリのテーマ", accent = "カラーソース", language = "言語",
            keepScreenOn = "再生中は画面をオンに保つ", autoplay = "自動再生",
            system = "システム", light = "ライト", dark = "ダーク", artworkColor = "アートワークの動的カラー",
            violet = "バイオレット", ocean = "オーシャン", sunset = "サンセット", forest = "フォレスト",
            queue = "キュー", clearQueue = "キューを消去", speed = "再生速度",
            permissionNeeded = "デバイスのメディアをスキャンするにはメディア権限が必要です。",
            playerReady = "再生準備完了", unableToPlay = "このメディアを再生できません", audio = "オーディオ", video = "ビデオ",
            automatic = "自動", dynamicNote = "埋め込みアートワーク、動画フレーム、または指定したアートワーク URL から実際の主要色を抽出します。",
            welcomeEyebrow = "あなたのサウンドスペース", welcomeTitle = "すべてのフレームに、\nサウンドトラックを。",
            welcomeBody = "デバイスのメディア、HTTP オーディオとビデオ、HLS ライブストリームを再生し、観ているものからインターフェースを彩りましょう。",
            welcomeStart = "聴き始める", welcomeFeatureOne = "シームレスなストリーミング", welcomeFeatureTwo = "自分だけのカラー"
        )
        AppLanguage.KOREAN -> en.copy(
            home = "홈", library = "라이브러리", settings = "설정", nowPlaying = "재생 중",
            addStream = "네트워크 미디어 추가", streamHint = "HTTP, HTTPS 또는 HLS (.m3u8) URL",
            title = "제목", artist = "아티스트 / 채널", artwork = "아트워크 이미지 URL (선택 사항)",
            mediaType = "미디어 유형", playNow = "지금 재생", cancel = "취소", recent = "최근 재생",
            forYou = "랜덤 추천", randomMediaHint = "카드를 탭하여 재생",
            favorites = "즐겨찾기", deviceMedia = "이 기기", scanDevice = "기기 미디어 검색",
            scanning = "검색 중…", emptyLibrary = "기기 미디어를 찾지 못했습니다",
            noRecent = "재생한 미디어가 여기에 표시됩니다.", noFavorites = "하트를 탭하여 여기에 미디어를 추가하세요.",
            theme = "앱 테마", accent = "색상 소스", language = "언어",
            keepScreenOn = "재생 중 화면 켜기", autoplay = "자동 재생",
            system = "시스템", light = "라이트", dark = "다크", artworkColor = "아트워크 동적 색상",
            violet = "바이올렛", ocean = "오션", sunset = "선셋", forest = "포레스트",
            queue = "재생 대기열", clearQueue = "대기열 지우기", speed = "재생 속도",
            permissionNeeded = "기기 미디어를 검색하려면 미디어 권한이 필요합니다.",
            playerReady = "재생 준비 완료", unableToPlay = "이 미디어를 재생할 수 없습니다", audio = "오디오", video = "비디오",
            automatic = "자동", dynamicNote = "내장 아트워크, 비디오 프레임 또는 제공된 아트워크 URL에서 실제 주요 색상을 추출합니다.",
            welcomeEyebrow = "나만의 사운드 공간", welcomeTitle = "모든 프레임에는\n사운드트랙이 필요합니다.",
            welcomeBody = "기기 미디어, HTTP 오디오와 비디오, HLS 라이브 스트림을 재생하고 보고 있는 화면에서 인터페이스 색상을 피워 보세요.",
            welcomeStart = "감상 시작", welcomeFeatureOne = "매끄러운 스트리밍", welcomeFeatureTwo = "나만의 색상"
        )
    }

    val home get() = copy.home
    val library get() = copy.library
    val settings get() = copy.settings
    val nowPlaying get() = copy.nowPlaying
    val addStream get() = copy.addStream
    val streamHint get() = copy.streamHint
    val title get() = copy.title
    val artist get() = copy.artist
    val artwork get() = copy.artwork
    val mediaType get() = copy.mediaType
    val playNow get() = copy.playNow
    val cancel get() = copy.cancel
    val recent get() = copy.recent
    val forYou get() = copy.forYou
    val randomMediaHint get() = copy.randomMediaHint
    val favorites get() = copy.favorites
    val deviceMedia get() = copy.deviceMedia
    val scanDevice get() = copy.scanDevice
    val scanning get() = copy.scanning
    val emptyLibrary get() = copy.emptyLibrary
    val noRecent get() = copy.noRecent
    val noFavorites get() = copy.noFavorites
    val theme get() = copy.theme
    val accent get() = copy.accent
    val language get() = copy.language
    val keepScreenOn get() = copy.keepScreenOn
    val autoplay get() = copy.autoplay
    val system get() = copy.system
    val light get() = copy.light
    val dark get() = copy.dark
    val artworkColor get() = copy.artworkColor
    val violet get() = copy.violet
    val ocean get() = copy.ocean
    val sunset get() = copy.sunset
    val forest get() = copy.forest
    val queue get() = copy.queue
    val clearQueue get() = copy.clearQueue
    val speed get() = copy.speed
    val permissionNeeded get() = copy.permissionNeeded
    val playerReady get() = copy.playerReady
    val unableToPlay get() = copy.unableToPlay
    val audio get() = copy.audio
    val video get() = copy.video
    val automatic get() = copy.automatic
    val dynamicNote get() = copy.dynamicNote
    val welcomeEyebrow get() = copy.welcomeEyebrow
    val welcomeTitle get() = copy.welcomeTitle
    val welcomeBody get() = copy.welcomeBody
    val welcomeStart get() = copy.welcomeStart
    val welcomeFeatureOne get() = copy.welcomeFeatureOne
    val welcomeFeatureTwo get() = copy.welcomeFeatureTwo
}
