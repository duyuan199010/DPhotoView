## DPhotoView

### A ImageView can dismiss while slide up or slide down.

Dependency
--
Add this to your module's `build.gradle` file:

```
compile 'com.duyuan:DPhotoView:1.0.3'
```
Usage
--
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
Screenshots
--
<img src="screenshots/screenshot1.gif" width="320" />

License
--
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.