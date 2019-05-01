package com.example.lmqmusic.utils.lrcHelper;

import java.util.List;

public interface ILrcBuilder {
    List<LrcRow> getLrcRows(String rawLrc);
}
