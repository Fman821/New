class AzkarItem {
  final String id;
  final String title;
  final String ar;
  final String am;
  final int repeat;
  final String source;
  final String audio;
  final String translit;

  AzkarItem({
    required this.id,
    required this.title,
    required this.ar,
    required this.am,
    this.repeat = 1,
    this.source = '',
    this.audio = '',
    this.translit = '',
  });

  factory AzkarItem.fromJson(Map<String, dynamic> json) {
    return AzkarItem(
      id: json['id']?.toString() ?? '',
      title: json['title'] ?? '',
      ar: (json['ar'] ?? '').toString().trim(),
      am: (json['am'] ?? '').toString().trim(),
      repeat: json['repeat'] is int ? json['repeat'] : int.tryParse(json['repeat']?.toString() ?? '1') ?? 1,
      source: json['source'] ?? '',
      audio: json['audio'] ?? '',
      translit: (json['translit'] ?? '').toString().trim(),
    );
  }
}

class AzkarCategory {
  final String id;
  final String title;
  final String subtitle;
  final String icon;
  final String image;
  final List<AzkarItem> items;

  AzkarCategory({
    required this.id,
    required this.title,
    required this.subtitle,
    required this.icon,
    this.image = '',
    required this.items,
  });

  factory AzkarCategory.fromJson(Map<String, dynamic> json) {
    var rawList = json['items'] as List<dynamic>? ?? [];
    List<AzkarItem> parsedItems = rawList.map((e) => AzkarItem.fromJson(e)).toList();

    return AzkarCategory(
      id: json['id'] ?? '',
      title: json['title'] ?? '',
      subtitle: json['subtitle'] ?? '',
      icon: json['icon'] ?? 'sun',
      image: json['image'] ?? '',
      items: parsedItems,
    );
  }
}

class DuaItem {
  final String id;
  final String title;
  final String ar;
  final String am;
  final int repeat;
  final String source;
  final String audio;

  DuaItem({
    required this.id,
    required this.title,
    required this.ar,
    required this.am,
    this.repeat = 1,
    this.source = '',
    this.audio = '',
  });

  factory DuaItem.fromJson(Map<String, dynamic> json) {
    return DuaItem(
      id: json['id']?.toString() ?? '',
      title: json['title'] ?? '',
      ar: (json['ar'] ?? '').toString().trim(),
      am: (json['am'] ?? '').toString().trim(),
      repeat: json['repeat'] is int ? json['repeat'] : int.tryParse(json['repeat']?.toString() ?? '1') ?? 1,
      source: json['source'] ?? '',
      audio: json['audio'] ?? '',
    );
  }
}

class AllahName {
  final int n;
  final String ar;
  final String tr;
  final String am;

  AllahName({
    required this.n,
    required this.ar,
    required this.tr,
    required this.am,
  });

  factory AllahName.fromJson(Map<String, dynamic> json) {
    return AllahName(
      n: json['n'] is int ? json['n'] : int.tryParse(json['n']?.toString() ?? '1') ?? 1,
      ar: (json['ar'] ?? '').toString().trim(),
      tr: (json['tr'] ?? '').toString().trim(),
      am: (json['am'] ?? '').toString().trim(),
    );
  }
}

enum AppThemeMode {
  sacredLight('የቀን ብርሃን (Sacred Light)', 'ነጭና ወርቃማ ለስላሳ የቀን እይታ'),
  midnightOttoman('የኦቶማን ሰማያዊ (Midnight Ottoman)', 'ጥልቅ ሰማያዊ ከመዳብና ሮዝ ወርቅ ጋር'),
  glassmorphism3D('ዘመናዊ ግላስ (Glassmorphism)', 'የብርጭቆ ዲዛይን በኤመራልድና ወርቅ ፍንጣቂ'),
  masjidArchitecture('የመስጊድ ቅስት (Masjid Arch)', 'የመስጊዶች ቅስት አርክቴክቸርና የተረጋጋ አረንጓዴ'),
  darkEmeraldNeon('ኤመራልድ ኒዮን (Dark Emerald)', 'ጥቁር ኤመራልድ ከሚያበራ ኒዮን ወርቅ ጋር');

  final String titleAm;
  final String descAm;

  const AppThemeMode(this.titleAm, this.descAm);

  String get title => titleAm;
  String get desc => descAm;
  String get description => descAm;
}
