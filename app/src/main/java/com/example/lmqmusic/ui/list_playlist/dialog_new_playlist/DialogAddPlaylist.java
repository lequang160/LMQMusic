package com.example.lmqmusic.ui.list_playlist.dialog_new_playlist;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.lmqmusic.R;
import com.example.lmqmusic.data.model.realm.PlayListRealmObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialogAddPlaylist extends DialogFragment {

    @BindView(R.id.editText)
    EditText mEditText;

    PlayListRealmObject playListRealmObject;

    CreatePlaylistListener mListener;

    public void setmListener(CreatePlaylistListener mListener) {
        this.mListener = mListener;
    }

    private DialogAddPlaylistViewModel mViewModel;

    public static DialogAddPlaylist newInstance() {
        return new DialogAddPlaylist();
    }
    public static DialogAddPlaylist newInstance(PlayListRealmObject playListRealmObject) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("PLAYLIST",playListRealmObject);
        DialogAddPlaylist dialogAddPlaylist = new DialogAddPlaylist();
        dialogAddPlaylist.setArguments(bundle);
        return  dialogAddPlaylist;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_add_playlist_fragment, container, false);
        ButterKnife.bind(this, rootView);
        getArgument();
        mEditText.setText(playListRealmObject != null ? playListRealmObject.getName() : "");

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DialogAddPlaylistViewModel.class);
        // TODO: Use the ViewModel
    }

    @OnClick(R.id.cancel)
    void clickCancel(){
        mListener.onDismiss();
        dismiss();
    }

    @OnClick(R.id.create)
    void clickCreate(){
        if(playListRealmObject != null) {
            if (mEditText.getText().toString().length() > 0) {
                long id = playListRealmObject.getId();
                String namePlaylist = mEditText.getText().toString();
                PlayListRealmObject object = new PlayListRealmObject(id,namePlaylist);
                object.setListSong(playListRealmObject.getListSong());
                mListener.onCreate(object);
            }
        }else{
            if (mEditText.getText().toString().length() > 0)
                mListener.onCreate(new PlayListRealmObject(mEditText.getText().toString()));
        }
        dismiss();
    }

    void getArgument()
    {
        Bundle bundle = getArguments();
        if(bundle!= null)
        {
            playListRealmObject = bundle.getParcelable("PLAYLIST");
        }
    }

    public interface  CreatePlaylistListener{
        void onDismiss();
        void onCreate(PlayListRealmObject playListRealmObject);
    }

}
