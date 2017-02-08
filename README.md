## DPhotoView

### A ImageView can dismiss while slide up or slide down.

###Screenshots
<img src="screenshots/1.gif" width="320" />

###Usage

```
final ArrayList<String> photos = new ArrayList<>();
photos.add("http://www.ymfarms.com/file/upload/201607/15/155720714959.jpg");
photos.add("http://img5q.duitang.com/uploads/item/201503/22/20150322103113_vAf2s.thumb.700_0.jpeg");
photos.add("http://img0.ph.126.net/8DKh0dJgAXtRZTkf_Xizpg==/6630316799257554651.jpg");
photos.add("http://img4q.duitang.com/uploads/item/201501/21/20150121114739_GNNj2.jpeg");
findViewById(R.id.photo).setOnClickListener(new View.OnClickListener() {
       @Override public void onClick(View view) {
            PhotoActivity.actionStart(MainActivity.this, photos);
       }
 });
```