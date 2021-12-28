package objectProtocol;

import domain.SpetacolDTO;

public class FestivalResponse implements Response{
    private Iterable<SpetacolDTO> festivalDTOS;

    public FestivalResponse(Iterable<SpetacolDTO> festivalDTOS) {
        this.festivalDTOS = festivalDTOS;
    }

    public Iterable<SpetacolDTO> getFestivalDTOS() {
        return festivalDTOS;
    }
}
